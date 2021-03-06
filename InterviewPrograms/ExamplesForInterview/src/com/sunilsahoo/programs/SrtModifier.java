package com.sunilsahoo.programs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class SrtModifier {
	static String sourceDirectory = "/Users/sunilkumarsahoo/Desktop/Game.of.Thrones.S07E04.HDTV.SVA.en.srt";
	// 00:55:52,354 --> 00:55:55,312
	static String format = "00:53:30,800 --> 00:53:33,835";
	static int modifyInSec = -15;
	static LinkedHashMap<String, List<Integer>> map = new LinkedHashMap<>();

	public static void main(String[] args) {

		File file = new File(sourceDirectory);
		List<String> fileList = new ArrayList<>();
		if (file.isDirectory()) {
			for (String path : file.list()) {
				if (path.endsWith(".srt")) {
					fileList.add(sourceDirectory + "/" + path);
				}
			}
		} else {
			fileList.add(sourceDirectory);
		}

		for (String path : fileList) {
			modifySrt(path, modifyInSec);
		}
	}

	private static void modifySrt(String path, int time) {
		for (int i = 0; i < format.length(); i++) {
			if ((format.charAt(i) == ':') || (format.charAt(i) == '-')
					|| (format.charAt(i) == '>') || (format.charAt(i) == ',')) {
				List<Integer> list = map.get("" + format.charAt(i));
				if (list == null) {
					list = new ArrayList<Integer>();
				}
				list.add(i);
				map.put("" + format.charAt(i), list);
			}
		}
		System.out.println("map : " + map);

		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			BufferedWriter wr = new BufferedWriter(
					new FileWriter(new File(path + 1)));
			String str;
			while ((str = br.readLine()) != null) {
				int counter = time / 60;
				int reminder = time % 60;
				counter = Math.abs(counter);
				StringBuilder input = new StringBuilder(str);
				if (reminder != 0) {
					for (int i = 0; i < counter - 1; i++) {
						input = modify(input, time < 0 ? -60 : 60);
					}
					input = modify(input, reminder);
				} else {
					for (int i = 0; i < counter; i++) {
						input = modify(input, time < 0 ? -60 : 60);
					}
				}
				str = input.toString();
				System.out.println("write : " + str);
				wr.write(str);
				wr.newLine();
			}
			br.close();
			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static StringBuilder modify(StringBuilder data, int sec) {
		boolean matches = true;
		if ((data != null) && data.length() == format.length()) {
			Set<Entry<String, List<Integer>>> set = map.entrySet();
			Iterator<Entry<String, List<Integer>>> itr = set.iterator();
			while (itr.hasNext()) {
				Entry<String, List<Integer>> entry = itr.next();
				List<Integer> list = entry.getValue();
				for (int i = 0; i < list.size(); i++) {
					if (!(String.valueOf(data.charAt(list.get(i)))
							.equals(entry.getKey()))) {
						matches = false;
					}
				}
			}
		} else {
			matches = false;
		}

		if (matches) {
			System.out.println("before data : " + data.toString());
			List<Integer> ls = map.get(":");
			int second = Integer
					.parseInt(data.substring(ls.get(1) + 1, ls.get(1) + 3));
			int minute = Integer
					.parseInt(data.substring(ls.get(0) + 1, ls.get(0) + 3));
			int hour = Integer
					.parseInt(data.substring(ls.get(0) - 2, ls.get(0)));
			int carry = 0;
			second = second - sec;

			if (second < 0) {
				second = 60 + second;
				carry = -1;
			} else if (second > 59) {
				second = second - 60;
				carry = 1;
			}
			if (carry == -1) {
				minute = minute - 1;
				if (minute < 0) {
					minute = 60 + minute;
					carry = -1;
				} else {
					carry = 0;
				}
			} else if (carry == 1) {
				minute = minute + 1;
				if (minute < 0) {
					minute = 60 + minute;
					carry = -1;
				} else if (minute > 59) {
					minute = minute - 60;
					carry = 1;
				} else {
					carry = 0;
				}
			}
			if (carry == -1) {
				hour = hour - 1;
			} else if (carry == 1) {
				hour = hour + 1;
			}
			System.out.println(
					hour + ":::" + minute + " formatted sec : " + second);
			String formattedSec = format(second);

			String formattedMin = format(minute);
			String formattedHour = format(hour);
			System.out.println(formattedHour + ":::" + formattedMin
					+ " formatted sec : " + formattedSec);
			data.replace(ls.get(1) + 1, ls.get(1) + 3, formattedSec);
			data.replace(ls.get(0) + 1, ls.get(0) + 3, formattedMin);
			data.replace(ls.get(0) - 2, ls.get(0), formattedHour);

			System.out.println(data + " ::: " + ls);

			second = Integer
					.parseInt(data.substring(ls.get(3) + 1, ls.get(3) + 3));
			minute = Integer
					.parseInt(data.substring(ls.get(2) + 1, ls.get(2) + 3));
			hour = Integer.parseInt(data.substring(ls.get(2) - 2, ls.get(2)));
			carry = 0;
			second = second - sec;

			if (second < 0) {
				second = 60 + second;
				carry = -1;
			} else if (second > 59) {
				second = second - 60;
				carry = 1;
			}
			if (carry == -1) {
				minute = minute - 1;
				if (minute < 0) {
					minute = 60 + minute;
					carry = -1;
				} else {
					carry = 0;
				}
			} else if (carry == 1) {
				minute = minute + 1;
				if (minute < 0) {
					minute = 60 + minute;
					carry = -1;
				} else if (minute > 59) {
					minute = minute - 60;
					carry = 1;
				} else {
					carry = 0;
				}
			}
			if (carry == -1) {
				hour = hour - 1;
			} else if (carry == 1) {
				hour = hour + 1;
			}

			formattedSec = format(second);
			formattedMin = format(minute);
			formattedHour = format(hour);

			data.replace(ls.get(3) + 1, ls.get(3) + 3, formattedSec);
			data.replace(ls.get(2) + 1, ls.get(2) + 3, formattedMin);
			data.replace(ls.get(2) - 2, ls.get(2), formattedHour);
		}
		System.out.println("data : " + data.toString());
		return data;

	}

	static String format(int time) {
		if (time < 10) {
			return "0" + time;
		} else {
			return "" + time;
		}
	}
}
