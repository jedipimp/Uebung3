package com.example.dam.uebung3.Model;

import java.util.List;

/**
 * Created by Mat on 20.01.2016.
 */
public class CellTowers {
    List<CellTower> cellTowers;

    public List<CellTower> getCellTowers() {
        return cellTowers;
    }

    public void setCellTowers(List<CellTower> cellTowers) {
        this.cellTowers = cellTowers;
    }

    @Override
    public String toString() {
        return "CellTowers{" +
                "cellTowers=" + cellTowers +
                '}';
    }
}
