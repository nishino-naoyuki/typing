package jp.ac.asojuku.typing.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.ac.asojuku.typing.config.MessageProperty;
import jp.ac.asojuku.typing.dto.LoginInfoDto;
import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.form.LoginForm;
import jp.ac.asojuku.typing.param.SessionConst;
import jp.ac.asojuku.typing.service.UserService;

@Controller
public class LoginController {

	Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	HttpSession session;
	@Autowired
	UserService userService;
	
	@RequestMapping(value= {"/signin"}, method=RequestMethod.GET)
    public ModelAndView signin(
    		ModelAndView mv,
    		@ModelAttribute("msg")String msg,
    		@ModelAttribute("mail")String mail,
    		HttpServletRequest request,
    		HttpServletResponse response
    		) throws SystemErrorException {
		
        
        //エラーメッセージがあればメッセージを仕込んでおく
        if( msg != null && msg.length() > 0) {
        	mv.addObject("msg", msg);
        }else {
        	mv.addObject("msg", "");
        }
        LoginForm form = new LoginForm();
        form.setMail(mail);
    	mv.addObject("loginForm", form);
        mv.setViewName("sign-in");
        
		return mv;
	}

	@RequestMapping(value= {"/signout"}, method=RequestMethod.GET)
    public ModelAndView signout(
    		ModelAndView mv
    		) throws SystemErrorException {

		// ログイン情報を削除する
		session.removeAttribute(SessionConst.LOGININFO);
		
		mv.setViewName("redirect:signin");
        
		return mv;
	}

	/**
	 * @param redirectAttributes
	 * @param form
	 * @param mv
	 * @param request
	 * @param response
	 * @return
	 * @throws AZCafeException
	 */
	@RequestMapping(value= {"/auth"}, method=RequestMethod.POST)
	public String auth(
			RedirectAttributes redirectAttributes,
			LoginForm form,
    		ModelAndView mv,
    		HttpServletRequest request,
    		HttpServletResponse response
			) throws SystemErrorException {
		String url;
		
		url = "redirect:dashboard";
		
		//認証
		LoginInfoDto loginInfo = userService.login(form.getMail(), form.getPassword());
		if( loginInfo != null) {
			//セッションにログイン情報を保存
			session.setAttribute(SessionConst.LOGININFO,loginInfo);
			url = "redirect:dashboard";
		}else {
			url = fowardLoginError(redirectAttributes,form);
		}
		return url;
	}

	/**
	 * ログインエラー時の処理
	 * 
	 * @param redirectAttributes
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	private String fowardLoginError(RedirectAttributes redirectAttributes,LoginForm form) throws SystemErrorException {
		//エラーメッセージを取得
		String errMsg = MessageProperty.getInstance().getProperty(MessageProperty.LOGIN_ERR_LOGINERR);
		//エラーメッセージをセット
		redirectAttributes.addFlashAttribute("msg", errMsg);
		redirectAttributes.addFlashAttribute("mail", form.getMail());
		
		return "redirect:signin";
	}
}
