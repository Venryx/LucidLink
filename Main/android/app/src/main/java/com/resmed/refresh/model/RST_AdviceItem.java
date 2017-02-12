package com.resmed.refresh.model;

import de.greenrobot.dao.*;
import java.util.*;
import com.resmed.refresh.ui.uibase.app.*;
import android.content.*;

public class RST_AdviceItem
{
	public enum AdviceTag {
		kAdviceTagAwakenings(6),
		kAdviceTagDuration(2),
		kAdviceTagOnset(3),
		kAdviceTagOther(7),
		kAdviceTagREM(4),
		kAdviceTagSWS(5),
		kAdviceTagSleep101(1);

		private int numVal;

		static {
			AdviceTag[] var0 = new AdviceTag[]{kAdviceTagSleep101, kAdviceTagDuration, kAdviceTagOnset, kAdviceTagREM, kAdviceTagSWS, kAdviceTagAwakenings, kAdviceTagOther};
		}

		private AdviceTag(int var3) {
			this.numVal = var3;
		}

		public static AdviceTag fromValue(int var0) throws IllegalArgumentException {
			try {
				AdviceTag var2 = values()[var0];
				return var2;
			} catch (ArrayIndexOutOfBoundsException var3) {
				throw new IllegalArgumentException("Unknown enum value :" + var0);
			}
		}

		public int getVal() {
			return this.numVal;
		}
	}
	public enum AdviceType {
		kAdviceTypeAdvice,
		kAdviceTypeTask;

		static {
			AdviceType[] var0 = new AdviceType[]{kAdviceTypeTask, kAdviceTypeAdvice};
		}

		public static AdviceType fromValue(int var0) throws IllegalArgumentException {
			try {
				AdviceType var2 = values()[var0];
				return var2;
			} catch (ArrayIndexOutOfBoundsException var3) {
				return kAdviceTypeAdvice;
			}
		}

		public int getVal() {
			return this.ordinal();
		}
	}



	private String articleUrl;
	private List<RST_Button> buttons;
	private int category;
	private String content;
	private transient DaoSession daoSession;
	private String detail;
	private int feedback;
	private String header;
	private String htmlContent;
	private RST_SleepEvent hypnogramInfo;
	private Long hypnogramInfo__resolvedKey;
	private String icon;
	private long id;
	private Long idAdviceItem;
	private String idUser;
	private transient RST_AdviceItemDao myDao;
	private int ordr;
	private RST_User rST_User;
	private String rST_User__resolvedKey;
	private boolean read;
	private long sessionId;
	private String subtitle;
	private Date timestamp;
	private String type;

	static int[] $SWITCH_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag;
	static /* synthetic */ int[] $SWITCH_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag() {
		final int[] $switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag = RST_AdviceItem.$SWITCH_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag;
		if ($switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag != null) {
			return $switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag;
		}
		final int[] $switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag2 = new int[RST_AdviceItem.AdviceTag.values().length];
		while (true) {
			try {
				$switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag2[RST_AdviceItem.AdviceTag.kAdviceTagAwakenings.ordinal()] = 6;
				try {
					$switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag2[RST_AdviceItem.AdviceTag.kAdviceTagDuration.ordinal()] = 2;
					try {
						$switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag2[RST_AdviceItem.AdviceTag.kAdviceTagOnset.ordinal()] = 3;
						try {
							$switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag2[RST_AdviceItem.AdviceTag.kAdviceTagOther.ordinal()] = 7;
							try {
								$switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag2[RST_AdviceItem.AdviceTag.kAdviceTagREM.ordinal()] = 4;
								try {
									$switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag2[RST_AdviceItem.AdviceTag.kAdviceTagSWS.ordinal()] = 5;
									try {
										$switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag2[RST_AdviceItem.AdviceTag.kAdviceTagSleep101.ordinal()] = 1;
										return RST_AdviceItem.$SWITCH_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag = $switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag2;
									}
									catch (NoSuchFieldError noSuchFieldError) {}
								}
								catch (NoSuchFieldError noSuchFieldError2) {}
							}
							catch (NoSuchFieldError noSuchFieldError3) {}
						}
						catch (NoSuchFieldError noSuchFieldError4) {}
					}
					catch (NoSuchFieldError noSuchFieldError5) {}
				}
				catch (NoSuchFieldError noSuchFieldError6) {}
			}
			catch (NoSuchFieldError noSuchFieldError7) {
				continue;
			}
			break;
		}
		return $switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag2;
	}

	static int[] $SWITCH_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceType;
	static /* synthetic */ int[] $SWITCH_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceType() {
		final int[] $switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceType = RST_AdviceItem.$SWITCH_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceType;
		if ($switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceType != null) {
			return $switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceType;
		}
		final int[] $switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceType2 = new int[RST_AdviceItem.AdviceType.values().length];
		while (true) {
			try {
				$switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceType2[RST_AdviceItem.AdviceType.kAdviceTypeAdvice.ordinal()] = 2;
				try {
					$switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceType2[RST_AdviceItem.AdviceType.kAdviceTypeTask.ordinal()] = 1;
					return RST_AdviceItem.$SWITCH_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceType = $switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceType2;
				}
				catch (NoSuchFieldError noSuchFieldError) {}
			}
			catch (NoSuchFieldError noSuchFieldError2) {
				continue;
			}
			break;
		}
		return $switch_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceType2;
	}

	public RST_AdviceItem() {
	}

	public RST_AdviceItem(final long id) {
		this.id = id;
	}

	public RST_AdviceItem(final long id, final long sessionId, final String header, final String type, final Date timestamp, final int ordr, final int category, final String detail, final String content, final String htmlContent, final String icon, final int feedback, final boolean read, final String subtitle, final String articleUrl, final String idUser, final Long idAdviceItem) {
		this.id = id;
		this.sessionId = sessionId;
		this.header = header;
		this.type = type;
		this.timestamp = timestamp;
		this.ordr = ordr;
		this.category = category;
		this.detail = detail;
		this.content = content;
		this.htmlContent = htmlContent;
		this.icon = icon;
		this.feedback = feedback;
		this.read = read;
		this.subtitle = subtitle;
		this.articleUrl = articleUrl;
		this.idUser = idUser;
		this.idAdviceItem = idAdviceItem;
	}

	public void __setDaoSession(final DaoSession daoSession) {
		this.daoSession = daoSession;
		RST_AdviceItemDao rst_AdviceItemDao;
		if (daoSession != null) {
			rst_AdviceItemDao = daoSession.getRST_AdviceItemDao();
		}
		else {
			rst_AdviceItemDao = null;
		}
		this.myDao = rst_AdviceItemDao;
	}

	public void addArrayOfButtons(final List<RST_Button> list) {
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.buttons == null) {
			this.getButtons();
		}
		final RST_ButtonDao rst_ButtonDao = this.daoSession.getRST_ButtonDao();
		for (int i = 0; i < list.size(); ++i) {
			((RST_Button)list.get(i)).setRST_AdviceItem(this);
		}
		rst_ButtonDao.insertOrReplaceInTx((Iterable<RST_Button>)list);
		this.buttons.addAll(list);
	}

	public void addButton(final RST_Button rst_Button) {
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.buttons == null) {
			this.getButtons();
		}
		final RST_ButtonDao rst_ButtonDao = this.daoSession.getRST_ButtonDao();
		rst_Button.setRST_AdviceItem(this);
		rst_ButtonDao.insertOrReplace(rst_Button);
		this.buttons.add(rst_Button);
	}

	public List<String> allAdviceTags() {
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < RST_AdviceItem.AdviceTag.values().length; ++i) {
			list.add(this.stringForAdviceTag(RST_AdviceItem.AdviceTag.fromValue(i)));
		}
		return list;
	}

	public List<String> allAdviceTypes() {
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < RST_AdviceItem.AdviceTag.values().length; ++i) {
			list.add(this.stringForAdviceType(RST_AdviceItem.AdviceType.fromValue(i)));
		}
		return list;
	}

	public void delete() {
		if (this.myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		if (this.hypnogramInfo == null) {
			this.getHypnogramInfo();
		}
		if (this.buttons != null) {
			final Iterator<RST_Button> iterator = this.buttons.iterator();
			while (iterator.hasNext()) {
				iterator.next().delete();
			}
		}
		final RST_SleepEventDao rst_SleepEventDao = this.daoSession.getRST_SleepEventDao();
		if (this.hypnogramInfo != null) {
			rst_SleepEventDao.delete(this.hypnogramInfo);
		}
		this.myDao.delete(this);
	}

	public String getArticleUrl() {
		return this.articleUrl;
	}

	public List<RST_Button> getButtons() {
		Label_0058: {
			if (this.buttons != null) {
				break Label_0058;
			}
			if (this.daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			final List<RST_Button> queryRST_AdviceItem_Buttons = this.daoSession.getRST_ButtonDao()._queryRST_AdviceItem_Buttons(this.id);
			synchronized (this) {
				if (this.buttons == null) {
					this.buttons = queryRST_AdviceItem_Buttons;
				}
				// monitorexit(this)
				return this.buttons;
			}
		}
		return null;
	}

	public int getCategory() {
		return this.category;
	}

	public String getContent() {
		return this.content;
	}

	public String getDetail() {
		return this.detail;
	}

	public int getFeedback() {
		return this.feedback;
	}

	public String getHeader() {
		return this.header;
	}

	public String getHtmlContent() {
		return this.htmlContent;
	}

	public RST_SleepEvent getHypnogramInfo() {
		final Long idAdviceItem = this.idAdviceItem;
		Label_0069: {
			if (this.hypnogramInfo__resolvedKey != null && this.hypnogramInfo__resolvedKey.equals(idAdviceItem)) {
				break Label_0069;
			}
			if (this.daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			final RST_SleepEvent hypnogramInfo = (RST_SleepEvent)this.daoSession.getRST_SleepEventDao().load(idAdviceItem);
			synchronized (this) {
				this.hypnogramInfo = hypnogramInfo;
				this.hypnogramInfo__resolvedKey = idAdviceItem;
				// monitorexit(this)
				return this.hypnogramInfo;
			}
		}
		return null;
	}

	public String getIcon() {
		return this.icon;
	}

	public long getId() {
		return this.id;
	}

	public Long getIdAdviceItem() {
		return this.idAdviceItem;
	}

	public String getIdUser() {
		return this.idUser;
	}

	public int getOrdr() {
		return this.ordr;
	}

	public RST_User getRST_User() {
		final String idUser = this.idUser;
		Label_0066: {
			if (this.rST_User__resolvedKey != null && this.rST_User__resolvedKey == idUser) {
				break Label_0066;
			}
			if (this.daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			final RST_User rst_User = (RST_User)this.daoSession.getRST_UserDao().load(idUser);
			synchronized (this) {
				this.rST_User = rst_User;
				this.rST_User__resolvedKey = idUser;
				// monitorexit(this)
				return this.rST_User;
			}
		}
		return null;
	}

	public boolean getRead() {
		return this.read;
	}

	public long getSessionId() {
		return this.sessionId;
	}

	public String getSubtitle() {
		return this.subtitle;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public String getType() {
		return this.type;
	}

	public void refresh() {
		if (this.myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		this.myDao.refresh(this);
	}

	public void resetButtons() {
		synchronized (this) {
			this.buttons = null;
		}
	}

	public void setAndSaveHypnogramInfo(final RST_SleepEvent hypnogramInfo) {
		if (this.daoSession == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		this.daoSession.getRST_SleepEventDao().insertOrReplace(hypnogramInfo);
		this.setHypnogramInfo(hypnogramInfo);
		this.update();
	}

	public void setArticleUrl(final String articleUrl) {
		this.articleUrl = articleUrl;
	}

	public void setCategory(final int category) {
		this.category = category;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public void setDetail(final String detail) {
		this.detail = detail;
	}

	public void setFeedback(final int feedback) {
		this.feedback = feedback;
	}

	public void setHeader(final String header) {
		this.header = header;
	}

	public void setHtmlContent(final String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public void setHypnogramInfo(final RST_SleepEvent hypnogramInfo) {
		synchronized (this) {
			this.hypnogramInfo = hypnogramInfo;
			Long id;
			if (hypnogramInfo == null) {
				id = null;
			}
			else {
				id = hypnogramInfo.getId();
			}
			this.idAdviceItem = id;
			this.hypnogramInfo__resolvedKey = this.idAdviceItem;
		}
	}

	public void setIcon(final String icon) {
		this.icon = icon;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setIdAdviceItem(final Long idAdviceItem) {
		this.idAdviceItem = idAdviceItem;
	}

	public void setIdUser(final String idUser) {
		this.idUser = idUser;
	}

	public void setOrdr(final int ordr) {
		this.ordr = ordr;
	}

	public void setRST_User(final RST_User rst_User) {
		synchronized (this) {
			this.rST_User = rst_User;
			String id;
			if (rst_User == null) {
				id = null;
			}
			else {
				id = rst_User.getId();
			}
			this.idUser = id;
			this.rST_User__resolvedKey = this.idUser;
		}
	}

	public void setRead(final boolean read) {
		this.read = read;
	}

	public void setSessionId(final long sessionId) {
		this.sessionId = sessionId;
	}

	public void setSubtitle(final String subtitle) {
		this.subtitle = subtitle;
	}

	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public String stringForAdviceTag(final RST_AdviceItem.AdviceTag adviceTag) {
		final Context applicationContext = RefreshApplication.getInstance().getApplicationContext();
		switch ($SWITCH_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceTag()[adviceTag.ordinal()]) {
			default: {
				return null;
			}
			case 1: {
				return applicationContext.getString(2131165375);
			}
			case 2: {
				return applicationContext.getString(2131165376);
			}
			case 3: {
				return applicationContext.getString(2131165377);
			}
			case 4: {
				return applicationContext.getString(2131165378);
			}
			case 5: {
				return applicationContext.getString(2131165379);
			}
			case 6: {
				return applicationContext.getString(2131165380);
			}
			case 7: {
				return applicationContext.getString(2131165381);
			}
		}
	}

	public String stringForAdviceType(final RST_AdviceItem.AdviceType adviceType) {
		final Context applicationContext = RefreshApplication.getInstance().getApplicationContext();
		switch ($SWITCH_TABLE$com$resmed$refresh$model$RST_AdviceItem$AdviceType()[adviceType.ordinal()]) {
			default: {
				return null;
			}
			case 2: {
				return applicationContext.getString(2131165373);
			}
			case 1: {
				return applicationContext.getString(2131165374);
			}
		}
	}

	public void update() {
		if (this.myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		this.myDao.update(this);
	}
}
