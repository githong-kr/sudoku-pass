package com.bale.sudokupass.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
@Data
public class Cell {
    private int x;
    private int y;
    private int value;
    private ArrayList<Integer> candidates;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = 0;

        int[] initialArray = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        this.candidates = (ArrayList<Integer>) Arrays.stream(initialArray)
                                .boxed()
                                .collect(Collectors.toList());
    }

    public Cell(int value) {
        this.value = value;
    }

    public Cell(ArrayList<Integer> candidates) {
        this.candidates = candidates;
    }

    public Cell(Cell cell) {
        this.x = cell.getX();
        this.y = cell.getY();
        this.value = cell.getValue();
        this.candidates = (ArrayList<Integer>) cell.getCandidates().clone();
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Cell) {
            return (this.x == ((Cell) o).x && this.y == ((Cell) o).y);
        } else {
            return false;
        }
    }
}
