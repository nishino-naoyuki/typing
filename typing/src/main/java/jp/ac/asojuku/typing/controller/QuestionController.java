package jp.ac.asojuku.typing.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.asojuku.typing.dto.LoginInfoDto;
import jp.ac.asojuku.typing.dto.QuestionDetailDto;
import jp.ac.asojuku.typing.dto.QuestionOutlineDto;
import jp.ac.asojuku.typing.exception.PermitionException;
import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.form.QuestionForm;
import jp.ac.asojuku.typing.form.ScoringForm;
import jp.ac.asojuku.typing.param.RoleId;
import jp.ac.asojuku.typing.param.SessionConst;
import jp.ac.asojuku.typing.param.json.ErrorField;
import jp.ac.asojuku.typing.param.json.ResultJson;
import jp.ac.asojuku.typing.service.QuestionService;

@Controller
@RequestMapping("/question")
public class QuestionController {
	Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	QuestionService questionService;
	
	@Autowired
	HttpSession session;
	
	/**
	 * 問題作成画面を表示する
	 * @param mv
	 * @return
	 * @throws SystemErrorException
	 */
	@RequestMapping(value= {"/create"}, method=RequestMethod.GET)
    public ModelAndView disp(
    		ModelAndView mv
    		) throws SystemErrorException {
		
        mv.setViewName("qcreate");
        
		return mv;
	}
	

	/**
	 * 問題を作成しDBへ挿入する
	 * @param mv
	 * @param qForm
	 * @param bindingResult
	 * @return
	 * @throws SystemErrorException
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value= {"/insert"}, method=RequestMethod.POST)
	@ResponseBody
    public Object insert(ModelAndView mv,
    		@Valid QuestionForm qForm,
			BindingResult bindingResult) throws SystemErrorException, JsonProcessingException  {
		//エラーがあったか？
		if( bindingResult.hasErrors() ) {
			return getJson(bindingResult);
		}
		
		//登録処理
		questionService.insert(qForm);
		
		return getJson(bindingResult);
	}

	/**
	 * 問題一覧の表示
	 * @param mv
	 * @return
	 * @throws SystemErrorException
	 */
	@RequestMapping(value= {"/list"}, method=RequestMethod.GET)
    public ModelAndView list(
    		ModelAndView mv
    		) throws SystemErrorException {

		//ログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		List<QuestionOutlineDto> qList = questionService.list(loginInfo.getUid(), loginInfo.getRole());
        
		mv.addObject("qList", qList);
		mv.setViewName("qlist");
		
		return mv;
	}

	/**
	 * 問題の一覧から詳細を取得する（あくまで問題の一覧画面からの遷移）
	 * @param mv
	 * @param id
	 * @return
	 * @throws SystemErrorException
	 * @throws PermitionException
	 */
	@RequestMapping(value= {"/detail"}, method=RequestMethod.GET)
    public ModelAndView detail(
    		ModelAndView mv,
    		@RequestParam(required = false) Integer id
    		) throws SystemErrorException, PermitionException {

		//ログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto)session.getAttribute(SessionConst.LOGININFO);
		
		QuestionDetailDto detailDto =
				questionService.getPracticeDetail(id,loginInfo.getRole(),loginInfo.getUid());
		
		if( detailDto == null ) {
			logger.info("権限無しページを閲覧しようとした。ユーザーID："+loginInfo.getUid()+" 問題ID："+id);
			throw new PermitionException("権限無しエラー");
		}
		mv.addObject("detailDto", detailDto);
		mv.setViewName("qdetail");
		
		return mv;
	}
	

	@RequestMapping(value= {"/scoring"}, method=RequestMethod.POST)
	@ResponseBody
    public Object scoring(
    		ModelAndView mv,
    		ScoringForm scoringForm
    		) throws SystemErrorException, PermitionException {
		
		return "OK";
	}
	
	/* ---private method--- */
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
