package org.iti.jxkh.deptbusiness.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import jxl.write.WriteException;
import org.iti.gh.common.util.ExportExcel;
import org.iti.gh.ui.listbox.YearListbox;
import org.iti.jxkh.business.report.AdviceWin;
import org.iti.jxkh.entity.JXKH_MEETING;
import org.iti.jxkh.entity.Jxkh_Award;
import org.iti.jxkh.entity.Jxkh_Report;
import org.iti.jxkh.entity.Jxkh_ReportDept;
import org.iti.jxkh.entity.Jxkh_ReportMember;
import org.iti.jxkh.service.JxkhReportService;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import com.iti.common.util.ConvertUtil;
import com.uniwin.framework.entity.WkTUser;

public class PersonWin extends Window implements AfterCompose {

	/**
	 * @author ZhangyuGuang
	 */
	private static final long serialVersionUID = -3373087402377032549L;
	private Textbox reportName;
	private Groupbox cxtj;
	private Listbox reportListbox, auditState, rank;
	private JxkhReportService jxkhReportService;
	private List<Jxkh_Report> reportList = new ArrayList<Jxkh_Report>();
	private WkTUser user;
	private Paging reportPaging;
	private YearListbox year;
	private String nameSearch, indicatorId, yearSearch;
	private Short auditStateSearch;
	private boolean isQuery = false;

	@Override
	public void afterCompose() {
		Components.wireVariables(this, this);
		Components.addForwards(this, this);
		year.initYearListbox("");
		user = (WkTUser) Sessions.getCurrent().getAttribute("user");// 获取当前登录用户
		reportListbox.setItemRenderer(new ReportRenderer());
		auditState.setItemRenderer(new auditStateRenderer());
		initWindow();
		String[] report_Types = { "", "调研报告", "分析报告" };
		List<String> reportTpyeList = new ArrayList<String>();
		for (int i = 0; i < 3; i++) {
			reportTpyeList.add(report_Types[i]);
		}
		rank.setModel(new ListModelList(reportTpyeList));
		rank.setSelectedIndex(0);
	}

	public void initWindow() {
		reportPaging.addEventListener("onPaging", new EventListener() {
			public void onEvent(Event arg0) throws Exception {
				reportList = jxkhReportService.findReportByKbNumberAll(user
						.getDept().getKdNumber(), reportPaging.getActivePage(),
						reportPaging.getPageSize());
				reportListbox.setModel(new ListModelList(reportList));
			}
		});
		int totalNum = jxkhReportService.findReportByKbNumberAll(user.getDept()
				.getKdNumber());
		reportPaging.setTotalSize(totalNum);
		reportList = jxkhReportService.findReportByKbNumberAll(user.getDept()
				.getKdNumber(), reportPaging.getActivePage(), reportPaging
				.getPageSize());
		reportListbox.setModel(new ListModelList(reportList));
		String[] a = { "","填写中", "待审核", "部门审核中", "部门通过", "部门不通过","业务办暂缓通过", "业务办通过", "业务办不通过",
		"归档" };
		List<String> auditStateList = new ArrayList<String>();
		for (int i = 0; i < 8; i++) {
			auditStateList.add(a[i]);
		}
		auditState.setModel(new ListModelList(auditStateList));
		auditState.setSelectedIndex(0);
	}

	public class auditStateRenderer implements ListitemRenderer {
		@Override
		public void render(Listitem item, Object data) throws Exception {
			if (data == null)
				return;
			String auditState = (String) data;
			item.setValue(auditState);
			Listcell c0 = new Listcell();
			if (auditState == null || auditState.equals(""))
				c0.setLabel("--请选择--");
			else {
				c0.setLabel(auditState);
			}
			item.appendChild(c0);
		}
	}

	// 报告列表渲染器
	public class ReportRenderer implements ListitemRenderer {

		@Override
		public void render(Listitem item, Object data) throws Exception {
			if (data == null)
				return;
			final Jxkh_Report report = (Jxkh_Report) data;
			item.setValue(report);
			Listcell c0 = new Listcell();
			Listcell c1 = new Listcell(item.getIndex() + 1 + "");
			Listcell c2 = new Listcell(report.getName().length()>10?report.getName().substring(0, 10)+"...":report.getName());
			c2.setTooltiptext(report.getName());
			c2.setStyle("color:blue");
			c2.addEventListener(Events.ON_CLICK, new EventListener() {
				public void onEvent(Event event) throws Exception {
					Listitem item = (Listitem) event.getTarget().getParent();
					Jxkh_Report report = (Jxkh_Report) item.getValue();
					AddReportWin w = (AddReportWin) Executions
							.createComponents(
									"/admin/personal/deptbusinessdata/report/addreport.zul",
									null, null);
					try {
						w.setReport(report);
						w.initWindow();
						w.doModal();
					} catch (SuspendNotAllowedException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (isQuery) {
						onClick$query();
					} else {
						initWindow();
					}
				}
			});
			//报告种类
			Listcell c3 = new Listcell(report.getType());
			//积分年度
			Listcell c4 = new Listcell();
			if (report.getjxYear() != null)
				c4 = new Listcell(report.getjxYear());
			//该项得分
			Listcell c5 = new Listcell(report.getScore() == null ? "" : report
					.getScore().toString());
			//填写人
			Listcell c6 = new Listcell();
			c6.setLabel(report.getSubmitName());
			//审核状态
			Listcell c7 = new Listcell();
			c7.setTooltiptext("点击查看审核结果");
			if (report.getState() == null || report.getState() == 0) {
				c7.setLabel("待审核");
				c7.setStyle("color:red");
			} else {
				switch (report.getState()) {
				case 0:
					break;
				case 1:
					c7.setLabel("部门通过");
					c7.setStyle("color:red");
					break;
				case 2:
					c7.setLabel("部门审核中");
					c7.setStyle("color:red");
					break;
				case 3:
					c7.setLabel("部门不通过");
					c7.setStyle("color:red");
					break;
				case 4:
					c7.setLabel("业务办通过");
					c7.setStyle("color:red");
					break;
				case 5:
					c7.setLabel("业务办不通过");
					c7.setStyle("color:red");
					break;
				case 6:
					c7.setLabel("归档");
					c7.setStyle("color:red");
					break;
				case JXKH_MEETING.WRITING:
					c7.setLabel("填写中");
					c7.setStyle("color:red");
					break;
				case JXKH_MEETING.BUSINESS_TEMP_PASS:
					c7.setLabel("业务办暂缓通过");
					c7.setStyle("color:red");
					break;

				}
			}
			// 弹出审核意见事件
			c7.addEventListener(Events.ON_CLICK, new EventListener() {
				public void onEvent(Event event) throws Exception {
					AdviceWin w = (AdviceWin) Executions.createComponents(
							"/admin/personal/businessdata/report/advice.zul",
							null, null);
					try {
						w.setMeeting(report);
						w.initWindow();
						w.doModal();
					} catch (SuspendNotAllowedException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			item.appendChild(c0);
			item.appendChild(c1);
			item.appendChild(c2);
			item.appendChild(c3);
			item.appendChild(c4);
			item.appendChild(c5);
			item.appendChild(c6);
			item.appendChild(c7);
		}
	}

	// 单击添加按钮，弹出添加报告界面
	public void onClick$add() {
		AddReportWin win = (AddReportWin) Executions.createComponents(
				"/admin/personal/deptbusinessdata/report/addreport.zul", null,
				null);
		try {

			win.initWindow();
			win.doModal();

		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		initWindow();
	}

	// 删除是弹出确定删除框
	public void onClick$del() throws InterruptedException {
		if (reportListbox.getSelectedItems() == null
				|| reportListbox.getSelectedItems().size() <= 0) {
			try {
				Messagebox.show("请选择要删除的报告！", "提示", Messagebox.OK,
						Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		Messagebox.show("确定删除吗?", "确定", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							@SuppressWarnings("unchecked")
							Iterator<Listitem> items = reportListbox
									.getSelectedItems().iterator();
							List<Jxkh_Report> reportList = new ArrayList<Jxkh_Report>();
							Jxkh_Report report = new Jxkh_Report();
							while (items.hasNext()) {
								report = (Jxkh_Report) items.next().getValue();
								reportList.add(report);
								if (report.getState() == Jxkh_Award.DEPT_PASS
										|| report.getState() == Jxkh_Award.First_Dept_Pass
										|| report.getState() == Jxkh_Award.BUSINESS_PASS || report.getState().shortValue() == JXKH_MEETING.BUSINESS_TEMP_PASS
										|| report.getState() == Jxkh_Award.SAVE_RECORD) {
									try {
										Messagebox.show(
												"部门或业务办审核通过或归档的奖励不能删除！", "提示",
												Messagebox.OK,
												Messagebox.INFORMATION);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									return;
								}
							}
							jxkhReportService.deleteAll(reportList);
							try {
								Messagebox.show("奖励删除成功！", "提示", Messagebox.OK,
										Messagebox.INFORMATION);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							initWindow();
						}
					}
				});
	}

	public void onClick$query() {
		nameSearch = reportName.getValue();
		auditStateSearch = null;
		isQuery = true;
		if (auditState.getSelectedItem().getValue().equals("")) {
			auditStateSearch = null;
		} else if (auditState.getSelectedItem().getValue().equals("待审核")) {
			auditStateSearch = 0;
		} else if (auditState.getSelectedItem().getValue().equals("部门通过")) {
			auditStateSearch = 1;
		} else if (auditState.getSelectedItem().getValue().equals("部门审核中")) {
			auditStateSearch = 2;
		} else if (auditState.getSelectedItem().getValue().equals("部门不通过")) {
			auditStateSearch = 3;
		} else if (auditState.getSelectedItem().getValue().equals("业务办通过")) {
			auditStateSearch = 4;
		} else if (auditState.getSelectedItem().getValue().equals("业务办不通过")) {
			auditStateSearch = 5;
		} else if (auditState.getSelectedItem().getValue().equals("归档")) {
			auditStateSearch = 6;
		}else if (auditState.getSelectedItem().getValue().equals("填写中")) {
			auditStateSearch = JXKH_MEETING.WRITING;
		}else if (auditState.getSelectedItem().getValue().equals("业务办暂缓通过")) {
			auditStateSearch = JXKH_MEETING.BUSINESS_TEMP_PASS;
		}
		indicatorId = null;
		if (rank.getSelectedIndex() != 0) {
			indicatorId = rank.getSelectedItem().getValue() + "";
		}
		yearSearch = null;
		if (year.getSelectedIndex() != 0 && year.getSelectedItem() != null)
			yearSearch = year.getSelectedItem().getValue() + "";

		reportPaging.addEventListener("onPaging", new EventListener() {
			public void onEvent(Event arg0) throws Exception {
				reportList = jxkhReportService.findReportByCondition(
						nameSearch, auditStateSearch, indicatorId, yearSearch,
						user.getDept().getKdNumber(),
						reportPaging.getActivePage(),
						reportPaging.getPageSize());
				reportListbox.setModel(new ListModelList(reportList));
			}
		});
		int totalNum = jxkhReportService.findReportByCondition(nameSearch,
				auditStateSearch, indicatorId, yearSearch, user.getDept()
						.getKdNumber());
		reportPaging.setTotalSize(totalNum);
		reportList = jxkhReportService.findReportByCondition(nameSearch,
				auditStateSearch, indicatorId, yearSearch, user.getDept()
						.getKdNumber(), reportPaging.getActivePage(),
				reportPaging.getPageSize());
		reportListbox.setModel(new ListModelList(reportList));
	}

	public void onClick$searchcbutton() {
		if (cxtj.isVisible()) {
			cxtj.setVisible(false);
		} else {
			cxtj.setVisible(true);
		}

	}

	public void onClick$reset() {
		reportName.setValue("");
		auditState.getItemAtIndex(0).setSelected(true);
		rank.getItemAtIndex(0).setSelected(true);
		year.getItemAtIndex(0).setSelected(true);
	}

	// 导出
	public void onClick$expert() throws WriteException, IOException {
		if (reportListbox.getSelectedCount() == 0) {
			try {
				Messagebox.show("提示请选择要导出的数据", "提示", Messagebox.OK,
						Messagebox.EXCLAMATION);
			} catch (InterruptedException e) {
				// ignore
			}
			return;
		} else {
			int i = 0;
			ArrayList<Jxkh_Report> expertlist = new ArrayList<Jxkh_Report>();
			@SuppressWarnings("unchecked")
			Set<Listitem> sel = reportListbox.getSelectedItems();
			Iterator<Listitem> it = sel.iterator();
			while (it.hasNext()) {
				Listitem item = (Listitem) it.next();
				Jxkh_Report p = (Jxkh_Report) item.getValue();
				expertlist.add(i, p);
				i++;
			}
			Date now = new Date();
			String fileName = ConvertUtil.convertDateString(now)
					+ "baogaoxinxi" + ".xls";
			String Title = "报告情况";
			List<String> titlelist = new ArrayList<String>();
			titlelist.add("序号");
			titlelist.add("报告名称");
			titlelist.add("完成人");
			titlelist.add("院内部门");
			titlelist.add("院外部门");
			titlelist.add("报告种类");
			titlelist.add("批示领导");
			titlelist.add("完成时间");
			titlelist.add("科学领域");
			titlelist.add("信息填写人");
			String c[][] = new String[expertlist.size()][titlelist.size()];
			// 从SQL中读数据
			for (int j = 0; j < expertlist.size(); j++) {
				Jxkh_Report report = (Jxkh_Report) expertlist.get(j);
				List<Jxkh_ReportMember> mlist = report.getReportMember();
				String member = "";
				c[j][0] = j + 1 + "";
				c[j][1] = report.getName();
				if (mlist.size() != 0) {
					for (int m = 0; m < mlist.size(); m++) {
						Jxkh_ReportMember mem = (Jxkh_ReportMember) mlist
								.get(m);
						member += mem.getName() + "、";
					}
					c[j][2] = member.substring(0, member.length() - 1);
				}
				List<Jxkh_ReportDept> dlist = report.getReprotDept();
				String dept = "";
				if (dlist.size() != 0) {
					for (int m = 0; m < dlist.size(); m++) {
						Jxkh_ReportDept de = (Jxkh_ReportDept) dlist.get(m);
						dept += de.getName() + "、";
					}
					c[j][3] = dept.substring(0, dept.length() - 1);
				}
				c[j][4] = report.getCoCompany();
				c[j][5] = report.getType();
				c[j][6] = report.getLeader().getKbName();
				c[j][7] = report.getDate();
				c[j][8] = report.getFiled();
				c[j][9] = report.getSubmitId();
			}
			ExportExcel ee = new ExportExcel();
			ee.exportExcel(fileName, Title, titlelist, expertlist.size(), c);
		}
	}

}
