package jp.ac.asojuku.typing.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.asojuku.typing.dto.EventInfoDto;
import jp.ac.asojuku.typing.dto.EventOutlineDto;
import jp.ac.asojuku.typing.dto.LoginInfoDto;
import jp.ac.asojuku.typing.dto.QuestionOutlineDto;
import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.form.EventCreateForm;
import jp.ac.asojuku.typing.param.RoleId;
import jp.ac.asojuku.typing.param.SessionConst;
import jp.ac.asojuku.typing.param.json.ErrorField;
import jp.ac.asojuku.typing.param.json.ResultJson;
import jp.ac.asojuku.typing.service.EventService;
import jp.ac.asojuku.typing.service.QuestionService;

@Controller
@RequestMapping("/event")
public class EventController {
	Logger logger = LoggerFactory.getLogger(EventController.class);
	@Autowired
	HttpSession session;
	@Autowired
	EventService eventService;
	@Autowired
	QuestionService questionService;
	/**
	 * イベントリストの表示
	 * @param mv
	 * @return
	 * @throws SystemErrorException
	 */
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public ModelAndView list(ModelAndView mv) throws SystemErrorException {

		// ログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto) session.getAttribute(SessionConst.LOGININFO);

		List<EventOutlineDto> eList = eventService.getList(loginInfo.getUid(), loginInfo.getMail(),
				loginInfo.getRole());
		
		//セッションにトークンとIDのペアを保存する
		//イベント登録時に使用するので学生権限の時のみ
		if( loginInfo.getRole() == RoleId.STUDENT) {
			setupTokenEid(eList);
		}

		mv.addObject("eList", eList);
		mv.setViewName("elist");

		return mv;
	}
	
	/**
	 * 作成画面
	 * @param mv
	 * @return
	 */
	@RequestMapping(value = { "/create" }, method = RequestMethod.GET)
	public ModelAndView create(ModelAndView mv) {
		
		List<QuestionOutlineDto> qList = questionService.listForEvent();
        
		mv.addObject("qList", qList);
		
		mv.setViewName("ecreate");
		return mv;
	}

	
	/**
	 * イベント作成処理
	 * @param mv
	 * @param eventCreateForm
	 * @param bindingResult
	 * @return
	 * @throws SystemErrorException
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = { "/create" }, method = RequestMethod.POST)
	@ResponseBody
	public Object inert(
			@Valid EventCreateForm eventCreateForm,
			BindingResult bindingResult) throws SystemErrorException, JsonProcessingException {

		//エラーがあったか？
		if( bindingResult.hasErrors() ) {
			return getJson(bindingResult);
		}
		
		eventService.insert(eventCreateForm);
		
		return getJson(bindingResult);
	}

	/**
	 * ユーザーの登録処理を行う
	 * @param token
	 * @return
	 */
	@RequestMapping(value = { "/entry" }, method = RequestMethod.POST)
	@ResponseBody
	public Object userEntry(
			@ModelAttribute("token")String token
			) {
		// ログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto) session.getAttribute(SessionConst.LOGININFO);
		
		//セッションからトークンを取得してイベントIDを取得する
		Map<String,Integer> eidMap = (Map<String,Integer>)session.getAttribute(SessionConst.EIDMAP);
				
		Integer eid = eidMap.get(token);
		String result = "OK";
		if( eid != null ) {
			if( eventService.isExistEventUser(eid, eid) ) {
				result = "EXIST";
			}else {
				questionService.entryEvent(loginInfo.getUid(), eid);
			}
		}else {
			result = "NG";
		}
		
		return "{\"result\":\""+result+"\"}";
	}


	@RequestMapping(value = { "/detail" }, method = RequestMethod.GET)
	public ModelAndView detail(
			ModelAndView mv,@ModelAttribute("eid")Integer eid) throws SystemErrorException {
		
		// ログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto) session.getAttribute(SessionConst.LOGININFO);
		
		//詳細情報を取得する
		EventInfoDto eventDetail = eventService.getDetail(eid, loginInfo.getRole());
		
		mv.addObject("eventDetail", eventDetail);

		mv.setViewName("edetail");
		return mv;
	}
	
	/* ----private---- */
	/**
	 * トークンとイベントIDのペアをセッションに保存する
	 * ほんとは期限付きでDBに保存するのがよさげだけどめんどくさいのでセッションにする
	 * セッションにしたことにより、一覧画面を複数開いた時に、問題が生じるが目をつむる
	 * @param eList
	 */
	private void setupTokenEid(List<EventOutlineDto> eList) {

		Map<String,Integer> eidMap = new HashMap<>();
		for( EventOutlineDto dto:eList) {
			eidMap.put(dto.getToken(), dto.getEid());
		}
		session.setAttribute(SessionConst.EIDMAP, eidMap);
	}
	/**
	 * JSON変換
	 * @param bindingResult
	 * @return
	 * @throws JsonProcessingException
	 */
	private String getJson(BindingResult bindingResult) throws JsonProcessingException {
		ResultJson result = new ResultJson();
		List<ErrorField> errList = new ArrayList<>();
		for(FieldError error : bindingResult.getFieldErrors() ) {
			ErrorField errField = new ErrorField();
			errField.setField( error.getField() );
			errField.setMsg( error.getDefaultMessage() );
			errList.add(errField);
		}
		result.setErrorList(errList);
		result.setResult( (errList.size() > 0 ? "NG":"OK") );

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(result);

        logger.trace("jsonString:{}",jsonString);

        return jsonString;
	}
}
