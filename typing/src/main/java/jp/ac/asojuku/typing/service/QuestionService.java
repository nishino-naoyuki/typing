package jp.ac.asojuku.typing.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.typing.entity.QestionTblEntity;
import jp.ac.asojuku.typing.form.QuestionForm;
import jp.ac.asojuku.typing.repository.QuestionRepository;

@Service
public class QuestionService {
	Logger logger = LoggerFactory.getLogger(QuestionService.class);
	
	@Autowired
	QuestionRepository questionRepository;
	
	/**
	 * 問題を1件挿入
	 * @param form
	 */
	public void insert(QuestionForm form) {
		questionRepository.saveAndFlush(getFrom(form));
	}
	
	/**
	 * FORM→Entity
	 * @param form
	 * @return
	 */
	private QestionTblEntity getFrom(QuestionForm form) {
		QestionTblEntity entity = new QestionTblEntity();
		
		entity.setTitle(form.getTitle());
		entity.setSentence(form.getQuestion());
		entity.setPracticeflg( (form.getPracticeFlg() ? 1:0) );
		entity.setDifficalty(form.getDifficulty());
		entity.setCount(form.getQuestion().length());
		
		return entity;
	}
}
