package com.bale.sudokupass.util;

import com.bale.sudokupass.web.dto.Cell;

import java.util.ArrayList;
import java.util.List;

public class SudokuRemoveUtil {
    public static void candidatesRemoverByValue(List<Cell> cellList, Cell targetCell) {
        for(Cell cell : cellList) {
            targetCell.getCandidates().remove(Integer.valueOf(cell.getValue()));
        }
    }

    public static void candidatesRemoverByCandidates(List<Cell> cellList, Cell targetCell) {
        List<Cell> sameCandidatesCellList = new ArrayList<>();

        for(Cell cell : cellList) {
            if(targetCell.getCandidates().containsAll(cell.getCandidates())) {
                sameCandidatesCellList.add(cell);
            }
        }
        if(sameCandidatesCellList.size() == targetCell.getCandidates().size()) {
            for(Cell cell : cellList) {
                if(sameCandidatesCellList.contains(cell)) continue;
                cell.getCandidates().removeAll(targetCell.getCandidates());
            }
        }
    }

}
