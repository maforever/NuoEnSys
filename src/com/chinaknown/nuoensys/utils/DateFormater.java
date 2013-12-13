package com.chinaknown.nuoensys.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateFormater {
	public static DateFormater instance = new DateFormater();

	public DateFormater() {
	};

	public static DateFormater getInstatnce() {
		return instance;
	}

	// ����������Сʱ���� 2013-09-23 13:88
	public String getYMDHT() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	public String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	// ��ȡ������
	public String getYMD() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	// �������
	public String getYM() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	// ��ȡ������ ��ʽyyyy-MM-dd
	public String getY_M_D() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	// ��ȡ���� ��ʽyyyy-MM
	public String getY_M() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	// ����yyyy-MM-dd��õ�ǰ�������ڼ�
	// ����yyyy-MM-dd
	public String getWeekday(String date) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdw = new SimpleDateFormat("E");
		Date d = null;
		try {
			d = sd.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdw.format(d);
	}

	// ������� yyyy
	public String getYear() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	public  int getDaysBetween(String beginDate, String endDate)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date bDate = format.parse(beginDate);
		Date eDate = format.parse(endDate);
		Calendar d1 = new GregorianCalendar();
		d1.setTime(bDate);
		Calendar d2 = new GregorianCalendar();
		d2.setTime(eDate);
		int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
		int y2 = d2.get(Calendar.YEAR);
		if (d1.get(Calendar.YEAR) != y2) {
			d1 = (Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);// �õ������ʵ������
				d1.add(Calendar.YEAR, 1);
			} while (d1.get(Calendar.YEAR) != y2);
		}
		return days;
	}
}
