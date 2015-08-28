package com.weson.util.date;

import com.weson.util.db.DBUtil;

public class Test {
	
	public static void main(String[] args) {
		DateUtil date = new DateUtil();
		System.out.println(date);
		DateUtil dateUtil = date.addMonths(-1);
		DateUtil dateUtil1 = date.addMonths(-2);
		DateUtil dateUtil2 = date.addDays(-6);
		String timeStamp = dateUtil.toString("yyyy-MM-dd HH:mm:ss");
		String timeStamp1 = dateUtil1.toString("yyyy-MM-dd HH:mm:ss");
		String timeStamp2 = dateUtil2.toString("yyyy-MM-dd HH:mm:ss");
		
		System.out.println(timeStamp);
		
		DBUtil dbUtil = new DBUtil();
		String activitysncode_sql = "delete from t_activitysncode where aid in (select id from t_activity where enddate<\"" + timeStamp +"\")limit 10000";
		String bargainactorrecord_sql = "delete from t_bargainactorrecord where actorId in (select id from t_bargainactor where configId in (select id from t_bargainconfig where delistTime <\"" + timeStamp +"\"))limit 10000";
		String redpacksendlog_sql = "delete from t_redpacksendlog where reqtime<\"" + timeStamp1 + "\" limit 10000";
		String crmftalk_sql = "delete from t_crmftalk where time<\""+ timeStamp2 +"\" limit 10000"; 
		try { 
			for(int i=0;i<10000;i++)
			{
				int count = dbUtil.delete(activitysncode_sql);
				if(count<10000) break;
			}
			for(int i=0;i<10000;i++)
			{
				int count = dbUtil.delete(bargainactorrecord_sql);
				if(count<10000) break;
			}
			for(int i = 10 ;i<10000;i++)
			{
				int count= dbUtil.delete(redpacksendlog_sql);
				if(count<10000) break;
			}
			
			for(int i = 0;i<10000;i++)
			{
				int count = dbUtil.delete(crmftalk_sql);
				if(count <10000) break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
