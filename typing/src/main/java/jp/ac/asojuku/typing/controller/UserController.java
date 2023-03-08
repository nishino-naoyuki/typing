package jp.ac.asojuku.typing.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jp.ac.asojuku.typing.dto.LoginInfoDto;
import jp.ac.asojuku.typing.dto.UserDetailInfoDto;
import jp.ac.asojuku.typing.param.SessionConst;
import jp.ac.asojuku.typing.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	HttpSession session;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value= {"/detail"}, method=RequestMethod.GET)
	public ModelAndView getDetail(ModelAndView mv,@ModelAttribute("uid")Integer uid) {
		// ログイン情報を取得する
		LoginInfoDto loginInfo = (LoginInfoDto) session.getAttribute(SessionConst.LOGININFO);

		UserDetailInfoDto userDetail =  userService.getUserDetail(uid);
		
		//編集可能かどうかを設定する
		if( loginInfo.isAdmin() ) {
			userDetail.setEditable(true);
		}else if( loginInfo.getUid() == uid) {
			userDetail.setEditable(true);
		}else {
			userDetail.setEditable(false);
		}
		
		mv.addObject("userDetail", userDetail);
		mv.setViewName("userInfo");

		return mv;
	}
}
