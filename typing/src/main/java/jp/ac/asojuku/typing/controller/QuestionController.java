package jp.ac.asojuku.typing.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.form.QuestionForm;
import jp.ac.asojuku.typing.param.json.ErrorField;
import jp.ac.asojuku.typing.param.json.ResultJson;
import jp.ac.asojuku.typing.service.QuestionService;

@Controller
@RequestMapping("/qcreate")
public class QuestionController {
	Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	QuestionService questionService;
	
	@RequestMapping(value= {""}, method=RequestMethod.GET)
    public ModelAndView disp(
    		ModelAndView mv
    		) throws SystemErrorException {
		
        mv.setViewName("qcreate");
        
		return mv;
	}
	

	@RequestMapping(value= {"/insert"}, method=RequestMethod.POST)
	@ResponseBody
    public Object insert(ModelAndView mv,
    		@Valid QuestionForm qForm,
			BindingResult bindingResult) throws SystemErrorException, JsonProcessingException  {
		//エラーがあったか？
		if( bindingResult.hasErrors() ) {
			return getErrorJson(bindingResult);
		}
		
		//登録処理
		questionService.insert(qForm);
		
		return getErrorJson(bindingResult);
	}
	
	/**
	 * JSON変換
	 * @param bindingResult
	 * @return
	 * @throws JsonProcessingException
	 */
	private String getErrorJson(BindingResult bindingResult) throws JsonProcessingException {
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
