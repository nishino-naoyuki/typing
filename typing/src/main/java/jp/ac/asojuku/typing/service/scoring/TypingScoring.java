package jp.ac.asojuku.typing.service.scoring;


import org.apache.lucene.search.spell.LevensteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.ac.asojuku.typing.dto.ScoringResultDto;


public class TypingScoring implements Scoring {
	Logger logger = LoggerFactory.getLogger(TypingScoring.class);
	/**
	 * 得点の計算方法
	 * 入力文字とお手本文字を比較して以下のように計算する
	 * 〇入力文字正答率（１００点満点）
	 * レーベンシュタイン距離で計算し類似度×１００を粗点とする
	 * 
	 * 〇入力スピード
	 * かかった時間と文字数を計算し、wpmを算出。その点数を加算する
	 * 
	 * 〇減点
	 * 入力文字数とキーアップの回数の乖離が大きい場合（具体的にはキーアップの回数が文字数の９割以下）の場合は
	 * 不正とみなし、０点とする
	 */
	@Override
	public ScoringResultDto doScoring(AnswerSheet ansSheet) {
		
		TypingAnswerSheet typingAnsSheet = (TypingAnswerSheet)ansSheet;

		ScoringResultDto resultDto = new ScoringResultDto();
		//〇入力文字正答率（１００点満点）
		/* クライアント側ですべて正しく入力しないと提出できないようにしたので固定で100点とする
		 * int score = getSimilarScoreByLevenshteinDistance(
		 * typingAnsSheet.getModelAns(), typingAnsSheet.getInputAns() );
		 */
		int score = 100;
		resultDto.setAccuracyScore(score);
		
		//〇入力スピード
		double inputAnsLen = typingAnsSheet.getInputAns().length();
		double time = typingAnsSheet.getTime();
		double wpm = inputAnsLen/(time/60);
		resultDto.setSppedScore(wpm);
		
		logger.info("inputAnsLen:"+inputAnsLen+" time:"+time+" wpm:"+wpm);
		//不正があったか？
		if( typingAnsSheet.getKeyupCount() < (int)inputAnsLen*0.9 ) {
			score = 0;
			resultDto.setUnjustFlag(true);
		}else {
			score += (int)wpm;
			resultDto.setUnjustFlag(false);
		}
		
		
		resultDto.setTotalScore(score);
		
		return resultDto;
	}
	
	/**
     * レーベンシュタイン距離で文字列の類似度を判定
     * @param s1
     * @param s2
     * @return 
     */
    private int getSimilarScoreByLevenshteinDistance(String s1, String s2){
        // 入力チェックは割愛
        LevensteinDistance dis =  new LevensteinDistance();
        return (int) (dis.getDistance(s1, s2) * 100);
    }

}
