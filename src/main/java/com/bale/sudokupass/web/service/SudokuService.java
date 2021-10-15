package com.bale.sudokupass.web.service;

import com.bale.sudokupass.constant.SudokuConstant;
import com.bale.sudokupass.util.SudokuPrintUtil;
import com.bale.sudokupass.util.SudokuRemoveUtil;
import com.bale.sudokupass.util.SudokuSearchUtil;
import com.bale.sudokupass.web.dto.Cell;
import com.bale.sudokupass.web.dto.SudokuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class SudokuService {

    private final SudokuDto sudokuDto;

    public void solve() {

        boolean isModified;
        int loopCount = 0;
        do {
            if(loopCount++ > SudokuConstant.MAX_LOOP_COUNT) {
                break;
            }

            buildCandidates();

            SudokuPrintUtil.printSudokuDto(sudokuDto, "1. 후보지 세팅");

            // 후보지 정리 및 단일 후보 값 세팅 반복
            do {
                isModified = setCellValueByOneCandidate();
            } while(isModified);

            SudokuPrintUtil.printSudokuDto(sudokuDto, "2. 단일 후보지 값 세팅");

            // 후보지 정리 및 유일 후보 값 세팅 반복
            do {
                isModified = setCellValueByUniqueCandidates();
            } while(isModified);

            SudokuPrintUtil.printSudokuDto(sudokuDto, "3. 순환 후보지 값 세팅");

        } while(SudokuSearchUtil.isFinished(sudokuDto));

        selectOnePath();

        if(SudokuSearchUtil.isFinished(sudokuDto)) {
            System.out.println("SUCCESS!!!!");
        } else {
            System.out.println("OTL.....");
        }

    }

    // 모르겠다!!! 찍자!!!
    private void selectOnePath() {
        Cell targetCell = null;
        Cell beforeCell = null;
        int guessIndex = 0;

        Stack<Cell> targetCellStack = new Stack<>();
        Stack<ArrayList<ArrayList<Cell>>> cellListHistoryStack = new Stack<>();

        while(!SudokuSearchUtil.isFinished(sudokuDto)) {

            for(int x = 0 ; x < SudokuConstant.SUDOKU_SIZE ; x++) {
                for(int y = 0 ; y < SudokuConstant.SUDOKU_SIZE ; y++) {
                    targetCell = sudokuDto.getSudokuList().get(x).get(y);

                    if(targetCell.getValue() == 0 && targetCell.getCandidates().size() == 2) {
                        if(!targetCellStack.contains(targetCell) && !targetCell.equals(beforeCell)) {
                            ArrayList<ArrayList<Cell>> originalCellList = sudokuDto.deepCopy();
                            cellListHistoryStack.push(originalCellList);
                            targetCellStack.push(new Cell(targetCell));
                        }

                        if(targetCell.equals(beforeCell)) {
                            guessIndex = 1;
                        }

                        targetCell.setValue(targetCell.getCandidates().get(guessIndex));

                        boolean isModified;
                        int loopCount = 0;
                        do {
                            if (loopCount++ > SudokuConstant.MAX_LOOP_COUNT) {
                                break;
                            }

                            buildCandidates();

                            SudokuPrintUtil.printSudokuDto(sudokuDto, "1'. 후보지 세팅");

                            // 후보지 정리 및 단일 후보 값 세팅 반복
                            do {
                                isModified = setCellValueByOneCandidate();
                            } while(isModified);

                            SudokuPrintUtil.printSudokuDto(sudokuDto, "2'. 단일 후보지 값 세팅");

                            // 후보지 정리 및 유일 후보 값 세팅 반복
                            do {
                                isModified = setCellValueByUniqueCandidates();
                            } while(isModified);

                            SudokuPrintUtil.printSudokuDto(sudokuDto, "3'. 순환 후보지 값 세팅");

                        } while(SudokuSearchUtil.isFinished(sudokuDto));

                        if(SudokuSearchUtil.isFinished(sudokuDto)) {
                            return;
                        }

                    }
                }
            }
            if(!SudokuSearchUtil.isFinished(sudokuDto)) {
                beforeCell = targetCellStack.pop();
                ArrayList<ArrayList<Cell>> rollBackHistory = new ArrayList<>(cellListHistoryStack.pop());
                sudokuDto.setSudokuList(rollBackHistory);
            }
        }
    }

    // 이 구역(가로, 세로, 사각형)에 혼자인 후보지 찾아서 값 세팅
    private boolean setCellValueByUniqueCandidates() {
        boolean isModified = false;

        for(int x = 0 ; x < SudokuConstant.SUDOKU_SIZE ; x++) {
            for (int y = 0; y < SudokuConstant.SUDOKU_SIZE; y++) {
                Cell targetCell = sudokuDto.getSudokuList().get(x).get(y);

                List<Cell> rowCells = SudokuSearchUtil.getRowCells(sudokuDto, targetCell);
                List<Cell> colCells = SudokuSearchUtil.getColCells(sudokuDto, targetCell);
                List<Cell> squareCells = SudokuSearchUtil.getSquareCells(sudokuDto, targetCell);

                // 가로 Cell 들 중 targetCell 의 특정 후보지가 하나만 존재할 경우 값 세팅
                if(setCellValueByUniqueCandidate(rowCells, targetCell)) isModified = true;

                // 세로 Cell 들 중 targetCell 의 특정 후보지가 하나만 존재할 경우 값 세팅
                if(setCellValueByUniqueCandidate(colCells, targetCell)) isModified = true;

                // 사각형 Cell 들 중 targetCell 의 특정 후보지가 하나만 존재할 경우 값 세팅
                if(setCellValueByUniqueCandidate(squareCells, targetCell)) isModified = true;
            }
        }

        return isModified;
    }

    // 주어진 Cell 리스트 후보지 중 targetCell 의 후보지가 유일하다면 값 세팅
    private boolean setCellValueByUniqueCandidate(List<Cell> cellList, Cell targetCell) {
        boolean isModified = false;
        boolean isUnique;

        for(int targetCandidate : targetCell.getCandidates()) {
            isUnique = true;
            for (Cell cell : cellList) {
                if (targetCell.equals(cell)) continue;

                if (cell.getCandidates().contains(targetCandidate)) {
                    isUnique = false;
                    break;
                }
            }

            if (isUnique && targetCell.getValue() == 0) {
                setCellValue(targetCell, targetCandidate);
                isModified = true;
                break;
            }
        }

        return isModified;
    }

    // 후보지가 하나이고 값 세팅이 안 되어 있는 셀 값 세팅
    private boolean setCellValueByOneCandidate() {

        boolean isModified = false;

        for(int x = 0 ; x < SudokuConstant.SUDOKU_SIZE ; x++) {
            for(int y = 0 ; y < SudokuConstant.SUDOKU_SIZE ; y++) {
                Cell targetCell = sudokuDto.getSudokuList().get(x).get(y);
                if(targetCell.getCandidates().size() == 1 &&
                   targetCell.getValue() == 0) {
                    setCellValue(targetCell, targetCell.getCandidates().get(0));
                    isModified = true;
                }
            }
        }

        return isModified;
    }

    // 값 세팅이 된 셀들을 참조하여 전체 후보지 정리
    private void buildCandidates() {
        for(int x = 0 ; x < SudokuConstant.SUDOKU_SIZE ; x++) {
            for(int y = 0 ; y < SudokuConstant.SUDOKU_SIZE ; y++) {
                Cell targetCell = sudokuDto.getSudokuList().get(x).get(y);
                setCandidates(targetCell);
            }
        }
    }

    // targetCell 기준으로 연관되는 셀들의 값을 참조하여 후보지 정리
    private void setCandidates(Cell targetCell) {
        if(targetCell.getValue() != 0) {
            // 해당 셀 후보지를 값으로 초기화
            ArrayList<Integer> value = new ArrayList<>();
            value.add(targetCell.getValue());
            targetCell.setCandidates(value);
        } else {
            // Row 탐색 및 존재하는 값 후보지 제거
            SudokuRemoveUtil.candidatesRemoverByValue(SudokuSearchUtil.getRowCells(sudokuDto, targetCell), targetCell);
            // Col 탐색 및 존재하는 값 후보지 제거
            SudokuRemoveUtil.candidatesRemoverByValue(SudokuSearchUtil.getColCells(sudokuDto, targetCell), targetCell);
            // Square 탐색 및 존재하는 값 후보지 제거
            SudokuRemoveUtil.candidatesRemoverByValue(SudokuSearchUtil.getSquareCells(sudokuDto, targetCell), targetCell);
        }

        if(targetCell.getCandidates().size() > 1) {
            // Row 탐색 및 교차 후보지 외 후보지 제거
            SudokuRemoveUtil.candidatesRemoverByCandidates(SudokuSearchUtil.getRowCells(sudokuDto, targetCell), targetCell);
            // Col 탐색 및 교차 후보지 외 후보지 제거
            SudokuRemoveUtil.candidatesRemoverByCandidates(SudokuSearchUtil.getColCells(sudokuDto, targetCell), targetCell);
            // Square 탐색 및 교차 후보지 외 후보지 제거
            SudokuRemoveUtil.candidatesRemoverByCandidates(SudokuSearchUtil.getSquareCells(sudokuDto, targetCell), targetCell);
        }
    }

    // Cell 의 값 세팅 및 후보지 정리
    private void setCellValue(Cell targetCell, int value) {
        targetCell.setValue(value);
        buildCandidates();
    }
}
