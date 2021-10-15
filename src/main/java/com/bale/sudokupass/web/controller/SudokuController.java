package com.bale.sudokupass.web.controller;

import com.bale.sudokupass.constant.SudokuConstant;
import com.bale.sudokupass.util.SudokuPrintUtil;
import com.bale.sudokupass.web.dto.Cell;
import com.bale.sudokupass.web.dto.SudokuDto;
import com.bale.sudokupass.web.service.SudokuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class SudokuController {

    private final SudokuService sudokuService;
    private final SudokuDto sudokuDto;

    @GetMapping("/test")
    public SudokuDto test() {
        // 풀리지 않는 문제를 위한 테스트 API
        int[][] requestArray = {{4, 0, 0, 3, 0, 7, 0, 0, 6},
                                {0, 3, 0, 4, 0, 5, 0, 0, 0},
                                {0, 0, 0, 9, 0, 0, 0, 4, 0},
                                {2, 8, 0, 0, 0, 0, 4, 0, 0},
                                {0, 0, 5, 0, 0, 0, 0, 6, 0},
                                {3, 4, 9, 0, 0, 0, 7, 0, 0},
                                {9, 7, 0, 8, 2, 0, 0, 0, 0},
                                {0, 0, 0, 0, 0, 0, 0, 7, 4},
                                {6, 2, 0, 0, 0, 0, 9, 8, 0}};

        SudokuPrintUtil.printSudokuDto(sudokuDto, "Request");

        sudokuService.solve();

        SudokuPrintUtil.printSudokuDto(sudokuDto, "Response");

        List<Integer> resultArray = new ArrayList<>();
        for(List<Cell> cells : sudokuDto.getSudokuList()) {
            for(Cell cell : cells) {
                resultArray.add(cell.getValue());
            }
        }

        return sudokuDto;
    }

    @PostMapping ("/solve")
    public List<Integer> solve(@RequestBody String[] inputValue) {

        int[][] requestArray = new int[SudokuConstant.SUDOKU_SIZE][SudokuConstant.SUDOKU_SIZE];
        for(int x = 0 ; x < SudokuConstant.SUDOKU_SIZE ; x++) {
            for(int y = 0 ; y < SudokuConstant.SUDOKU_SIZE ; y++) {
                requestArray[x][y] = Integer.parseInt(inputValue[(x*SudokuConstant.SUDOKU_SIZE) + y]);
            }
        }

        for(int i = 0 ; i < SudokuConstant.SUDOKU_SIZE ; i++) {
            for(int j = 0 ; j < SudokuConstant.SUDOKU_SIZE ; j++) {
                sudokuDto.getSudokuList().get(i).get(j).setValue(requestArray[i][j]);
            }
        }

        SudokuPrintUtil.printSudokuDto(sudokuDto, "Request");

        sudokuService.solve();

        SudokuPrintUtil.printSudokuDto(sudokuDto, "Response");

        List<Integer> resultArray = new ArrayList<>();
        for(List<Cell> cells : sudokuDto.getSudokuList()) {
            for(Cell cell : cells) {
                resultArray.add(cell.getValue());
            }
        }

        return resultArray;
    }
}
