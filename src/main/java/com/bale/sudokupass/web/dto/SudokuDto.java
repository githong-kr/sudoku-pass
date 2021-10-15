package com.bale.sudokupass.web.dto;

import com.bale.sudokupass.constant.SudokuConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Getter
@Setter
@Component
public class SudokuDto {
    ArrayList<ArrayList<Cell>> sudokuList;

    public SudokuDto() {
        sudokuList = new ArrayList<>();

        for(int i = 0 ; i < SudokuConstant.SUDOKU_SIZE ; i++) {
            ArrayList<Cell> initCells = new ArrayList<>();
            for(int j = 0 ; j < SudokuConstant.SUDOKU_SIZE ; j++) {
                Cell initCell = new Cell(i, j);
                initCells.add(initCell);
            }
            sudokuList.add(initCells);
        }
    }

    public ArrayList<ArrayList<Cell>> deepCopy() {
        ArrayList<ArrayList<Cell>> newSudokuList = new ArrayList<>();

        for(int i = 0 ; i < SudokuConstant.SUDOKU_SIZE ; i++) {
            ArrayList<Cell> newCells = new ArrayList<>();
            for(int j = 0 ; j < SudokuConstant.SUDOKU_SIZE ; j++) {
                Cell newCell = new Cell(this.sudokuList.get(i).get(j));
                newCells.add(newCell);
            }
            newSudokuList.add(newCells);
        }

        return newSudokuList;
    }
}
