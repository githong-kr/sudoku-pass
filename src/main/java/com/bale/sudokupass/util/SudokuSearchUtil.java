package com.bale.sudokupass.util;

import com.bale.sudokupass.constant.SudokuConstant;
import com.bale.sudokupass.web.dto.Cell;
import com.bale.sudokupass.web.dto.SudokuDto;

import java.util.ArrayList;
import java.util.List;

public class SudokuSearchUtil {

    public static boolean isFinished(SudokuDto sudokuDto) {
        for(int x = 0 ; x < SudokuConstant.SUDOKU_SIZE ; x++) {
            for(int y = 0 ; y < SudokuConstant.SUDOKU_SIZE ; y++) {
                if(sudokuDto.getSudokuList().get(x).get(y).getValue() == 0)
                    return false;
            }
        }

        return true;
    }

    public static List<Cell> getRowCells(SudokuDto sudokuDto, Cell targetCell) {

        List<Cell> rowCells = new ArrayList<>();

        for(int y = 0; y < SudokuConstant.SUDOKU_SIZE ; y++) {
            Cell rowCell = sudokuDto.getSudokuList().get(targetCell.getX()).get(y);
            rowCells.add(rowCell);
        }

        return rowCells;
    }

    public static List<Cell> getColCells(SudokuDto sudokuDto, Cell targetCell) {

        List<Cell> colCells = new ArrayList<>();

        for(int x = 0 ; x < SudokuConstant.SUDOKU_SIZE ; x++) {
            Cell colCell = sudokuDto.getSudokuList().get(x).get(targetCell.getY());
            colCells.add(colCell);
        }

        return colCells;
    }

    public static List<Cell> getSquareCells(SudokuDto sudokuDto, Cell targetCell) {

        List<Cell> squareCells = new ArrayList<>();

        int subXInitVal = (int)((targetCell.getX()/3)*Math.sqrt(SudokuConstant.SUDOKU_SIZE));
        int subYInitVal = (int)((targetCell.getY()/3)*Math.sqrt(SudokuConstant.SUDOKU_SIZE));
        for(int subX = subXInitVal ; subX < subXInitVal + Math.sqrt(SudokuConstant.SUDOKU_SIZE) ; subX++) {
            for(int subY = subYInitVal ; subY < subYInitVal + Math.sqrt(SudokuConstant.SUDOKU_SIZE) ; subY++) {
                Cell squareCell = sudokuDto.getSudokuList().get(subX).get(subY);
                squareCells.add(squareCell);
            }
        }

        return squareCells;
    }
}
