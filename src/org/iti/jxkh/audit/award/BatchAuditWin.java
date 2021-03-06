package org.iti.jxkh.audit.award;

import java.util.ArrayList;
import java.util.List;

import org.iti.jxkh.entity.JXKH_MEETING;
import org.iti.jxkh.entity.Jxkh_Award;
import org.iti.jxkh.entity.Jxkh_AwardDept;
import org.iti.jxkh.service.JxkhAwardService;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Window;

import com.uniwin.framework.entity.WkTUser;

public class BatchAuditWin extends Window implements AfterCompose {

	/**
	 * @author ZhangyuGuang C
	 */
	private static final long serialVersionUID = 5151969361938519733L;
	private List<Jxkh_Award> awardList = new ArrayList<Jxkh_Award>();
	private List<Jxkh_AwardDept> meetDeptList = new ArrayList<Jxkh_AwardDept>();
	private Radio pass, back;
	private WkTUser user;
	private JxkhAwardService jxkhAwardService;

	@Override
	public void afterCompose() {
		Components.wireVariables(this, this);
		Components.addForwards(this, this);
		user = (WkTUser) Sessions.getCurrent().getAttribute("user");// 获取当前登录用户
		pass.setChecked(true);

	}

	public void onClick$submit() {
		Jxkh_Award meeting = new Jxkh_Award();
		for (int i = 0; i < awardList.size(); i++) {
			meeting = awardList.get(i);
			meetDeptList = jxkhAwardService.findAwardDeptByAwardId(meeting);
			int index = 0;
			for (int t = 1; t <= meetDeptList.size(); t++) {
				Jxkh_AwardDept dept = (Jxkh_AwardDept) meetDeptList.get(t - 1);
				if (user.getDept().getKdName().equals(dept.getName())) {
					index = dept.getRank();
				}
			}
			String s = meeting.getTempState();
			char[] myStr = s.toCharArray();
			if (pass.isChecked()) {
				myStr[index - 1] = '1';
				String s0 = new String(myStr);
				meeting.setTempState(s0);
				meeting.setState(JXKH_MEETING.First_Dept_Pass);
				jxkhAwardService.saveOrUpdate(meeting);

				int k = 1;
				String str = meeting.getTempState();
				for (int j = 0; j < meetDeptList.size(); j++) {
					if (str.charAt(j) != '1')
						k = 0;
				}
				if (k == 1)
					meeting.setState(JXKH_MEETING.DEPT_PASS);
			} else if (back.isChecked()) {
				myStr[index - 1] = '2';
				String s0 = new String(myStr);
				meeting.setTempState(s0);
				meeting.setState(JXKH_MEETING.DEPT_NOT_PASS);
			}
			jxkhAwardService.saveOrUpdate(meeting);
		}

		try {
			Messagebox.show("批量审核成功！", "提示", Messagebox.OK,
					Messagebox.INFORMATION);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Messagebox.show("批量审核失败，请重试！", "提示", Messagebox.OK,
						Messagebox.INFORMATION);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		this.onClose();
	}

	public List<Jxkh_Award> getAwardList() {
		return awardList;
	}

	public void setAwardList(List<Jxkh_Award> awardList) {
		this.awardList = awardList;
	}

	/**
	 * <li>功能描述：关闭当前窗口。
	 */
	public void onClick$close() {
		this.onClose();
	}
}
