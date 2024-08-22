select qid from question_tbl q WHERE q.title='練習用課題４（約８００文字）';

SELECT eqid from event_question WHERE qid=7;

select ansid from ans_tbl a WHERE eqid=6;

SELECT anshid FROM ans_history_tbl WHERE ansid in (select ansid from ans_tbl a WHERE eqid=6);

select count(*) from ans_history_tbl WHERE anshid in (SELECT anshid FROM ans_history_tbl WHERE ansid in (select ansid from ans_tbl a WHERE eqid=6));
delete from ans_history_tbl a WHERE a.anshid in (SELECT anshid FROM ans_history_tbl b WHERE b.ansid in (select ansid from ans_tbl a WHERE eqid=6));
delete from ans_tbl WHERE ansid in (select ansid from ans_tbl a WHERE eqid=6);
delete from event_question WHERE eqid=6;
delete from question_tbl WHERE qid=7;