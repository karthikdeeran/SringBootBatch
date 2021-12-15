package com.example.batch.util;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;

public class EmployeeCSVWriter {

	public static void main(String[] args) {
		writer();
	}

	public static void writer() {
		File file = new File(
				"C:\\Users\\karthik\\Downloads\\batch (1)\\SpringBatch\\src\\main\\resources\\csv\\Employee.csv");

		try {
			FileWriter outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile, ',', CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

			List<String[]> list = new ArrayList<String[]>();
			for (int i = 0; i < 2000000; i++) {
				String[] array = new String[2];
				array[0] = getNextAlphabet(i);
				array[1] = "" + i;
				list.add(array);
			}
			writer.writeAll(list);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getNextAlphabet(int i) {
		String value = null;
		if (i < 0) {
			value = "";
		} else {
			value = getNextAlphabet((i / 26) - 1) + (char) (65 + i % 26);
		}
		return value;
	}
}
