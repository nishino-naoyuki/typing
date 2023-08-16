package jp.ac.asojuku.typing.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.asojuku.typing.dto.CreateUserDto;
import jp.ac.asojuku.typing.dto.LoginInfoDto;
import jp.ac.asojuku.typing.dto.UserDetailInfoDto;
import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.form.UserCreateForm;
import jp.ac.asojuku.typing.param.RoleId;
import jp.ac.asojuku.typing.param.SessionConst;
import jp.ac.asojuku.typing.param.json.ErrorField;
import jp.ac.asojuku.typing.param.json.ResultJson;
import jp.ac.asojuku.typing.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	HttpSession session;
	
	@Autowired
	UserService userService;
	
	/**
	 * 詳細画面表示
	 * @param mv
	 * @param uid
	 * @return
	 */
	@RequestMapping(value= {"/detail"}, method=RequestMethod.GET)
	public ModelAndView getDetail(ModelAndView mv,@ModelAttribute("uid")Integer uid) {
		// ログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto) session.getAttribute(SessionConst.LOGININFO);

		UserDetailInfoDto userDetail =  userService.getUserDetail(uid);
		
		//編集可能かどうかを設定する
		if( loginInfo.isAdmin() ) {
			//管理者は全員の情報を編集可能
			userDetail.setEditable(true);
		}else if( Objects.equals(loginInfo.getUid(), uid) ) {
			//自分の情報は編集可能
			userDetail.setEditable(true);
		}else {
			userDetail.setEditable(false);
		}
		//トークンとユーザーIDのペアをセッションに保存する
		session.setAttribute(userDetail.getToken(), userDetail.getUid());
		
		mv.addObject("userDetail", userDetail);
		mv.setViewName("userInfo");

		return mv;
	}

	/**
	 * 詳細画面での更新処理
	 * @param userCreateForm
	 * @param bindingResult
	 * @return
	 * @throws SystemErrorException
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value= {"/detail"}, method=RequestMethod.POST)
	@ResponseBody
	public Object postDetail(
			@Valid UserCreateForm userCreateForm,
			BindingResult bindingResult
			) throws SystemErrorException, JsonProcessingException {
		// ログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto) session.getAttribute(SessionConst.LOGININFO);
		//セッションに保存されたトークンから更新対象のユーザーIDを取得
		Integer uid = (Integer)session.getAttribute(userCreateForm.getToken());
		if( uid == null || 
			( loginInfo.getRole() == RoleId.STUDENT && !uid.equals( loginInfo.getUid() ) ) ) {
			throw new SystemErrorException("権限無し");
		}
		if(bindingResult.hasErrors()) {
			return getJson(bindingResult);
		}

		//更新
		userService.update(uid, userCreateForm);
		
		return getJson(bindingResult);
		
	}

	/**
	 * JSON作成
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
