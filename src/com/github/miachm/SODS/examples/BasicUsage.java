package com.github.miachm.SODS.com.github.miachm.SODS.examples;

import com.github.miachm.SODS.com.github.miachm.SODS.spreadsheet.Range;
import com.github.miachm.SODS.com.github.miachm.SODS.spreadsheet.Sheet;
import com.github.miachm.SODS.com.github.miachm.SODS.spreadsheet.SpreadSheet;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BasicUsage{
    public static void main(String args[]){
        try {
            SpreadSheet spread = new SpreadSheet(new File("BasicExample.ods"));
            System.out.println("Number of sheets: " + spread.getNumSheets());

            List<Sheet> sheets = spread.getSheets();

            for (Sheet sheet : sheets) {
                System.out.println("In sheet " + sheet.getName());

                Range range = sheet.getDataRange();

                System.out.println(range.getValues());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}