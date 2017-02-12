package com.resmed.refresh.model.mappers;

import com.resmed.refresh.utils.*;
import java.util.*;
import com.resmed.refresh.model.json.*;
import com.resmed.refresh.model.*;

public class NightQuestionsMapper
{
	@Deprecated
	public static List<PreSleepQuestion> getPreSleepQuestions(final RST_NightQuestions rst_NightQuestions) {
		final ArrayList<PreSleepQuestion> list = new ArrayList<PreSleepQuestion>();
		for (final RST_QuestionItem rst_QuestionItem : rst_NightQuestions.getQuestions()) {
			final PreSleepQuestion preSleepQuestion = new PreSleepQuestion();
			preSleepQuestion.setAnswerId(1 + rst_QuestionItem.getAnswer());
			preSleepQuestion.setQuestionId(rst_QuestionItem.getQuestionId());
			Log.d("com.resmed.refresh.model", "Question JSON question ID= " + preSleepQuestion.getQuestionId() + " answer=" + preSleepQuestion.getAnswerId());
			AppFileLog.addTrace("SDTeam: 1.Question JSON question ID= " + preSleepQuestion.getQuestionId() + " answer=" + preSleepQuestion.getAnswerId());
			list.add(preSleepQuestion);
		}
		return list;
	}

	public static List<PreSleepQuestion> getPreSleepQuestions(final String s, final String s2) {
		ArrayList<PreSleepQuestion> list = new ArrayList<PreSleepQuestion>();
		try {
			final StringTokenizer stringTokenizer = new StringTokenizer(s, ",");
			final StringTokenizer stringTokenizer2 = new StringTokenizer(s2, ",");
			while (stringTokenizer.hasMoreElements()) {
				final String nextToken = stringTokenizer.nextToken();
				final String nextToken2 = stringTokenizer2.nextToken();
				if (nextToken2.length() == 0 || nextToken.length() == 0) {
					return list;
				}
				final PreSleepQuestion preSleepQuestion = new PreSleepQuestion();
				preSleepQuestion.setQuestionId(Integer.parseInt(nextToken));
				preSleepQuestion.setAnswerId(1 + Integer.parseInt(nextToken2));
				Log.d("com.resmed.refresh.model", "Question JSON index= " + preSleepQuestion.getQuestionId() + " answer=" + preSleepQuestion.getAnswerId());
				AppFileLog.addTrace("SDTeam:Question JSON index with incrementing Q answerId by 1 to map with backend= " + preSleepQuestion.getQuestionId() + " answer=" + preSleepQuestion.getAnswerId());
				list.add(preSleepQuestion);
			}
			return list;
		}
		catch (Exception ex) {
			list = null;
		}
		return list;
	}

	public static RST_NightQuestions processNightQuestions(final NightQuestions nightQuestions) {
		return processQuestions(nightQuestions.getPreSleepQuestion(), nightQuestions.getContentVersion());
	}

	public static RST_NightQuestions processNightQuestions(final List<PreSleepQuestion> list) {
		return processQuestions(list, 0);
	}

	private static RST_NightQuestions processQuestions(final List<PreSleepQuestion> list, final int version) {
		final RefreshModelController instance = RefreshModelController.getInstance();
		final RST_NightQuestions nightQuestionsItem = instance.createNightQuestionsItem();
		nightQuestionsItem.setVersion(version);
		for (int i = 0; i < list.size(); ++i) {
			final PreSleepQuestion preSleepQuestion = list.get(i);
			final RST_QuestionItem questionItem = instance.createQuestionItem(preSleepQuestion.getBackendId());
			questionItem.setOrdr(i);
			questionItem.setText(preSleepQuestion.getDisplayText());
			Log.d("com.resmed.refresh.model", "processQuestions " + list.size() + " size, received = " + preSleepQuestion.getDisplayText() + " ID = " + preSleepQuestion.getBackendId());
			for (int j = 0; j < preSleepQuestion.getDisplayAnswers().size(); ++j) {
				final DisplayAnswers displayAnswers = preSleepQuestion.getDisplayAnswers().get(j);
				final RST_DisplayItem rst_DisplayItem = new RST_DisplayItem();
				rst_DisplayItem.setDisplayId(displayAnswers.getId());
				rst_DisplayItem.setOrdr(j);
				rst_DisplayItem.setValue(displayAnswers.getText());
				questionItem.addDisplayItem(rst_DisplayItem);
			}
			nightQuestionsItem.update();
			instance.save();
		}
		return nightQuestionsItem;
	}
}
