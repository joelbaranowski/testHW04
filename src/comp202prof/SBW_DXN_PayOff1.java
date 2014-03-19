package comp202prof;

import model.*;
import java.awt.*;

public class SBW_DXN_PayOff1 implements IBoardLambda<Integer>{
    private int CORNER_WT = 10000;
    private int BORDER_WT = 100;
    private int CELL_WT = 5;
    private int OFF_CORNER_FACTOR = -10000;
    private int SIDE_CORNER_FACTOR = -100;


    private Dimension dim;
    private int [][] cellWts;



    public SBW_DXN_PayOff1(IBoardModel host) {
      dim = host.getDimension();
      cellWts = new int[dim.height][dim.width];
      for(int row=0;row<dim.height;row++)
      {
        for(int col=0;col<dim.width;col++)
        {
            cellWts[row][col] = corners(row, col, dim)? CORNER_WT: (borders(row, col, dim)? BORDER_WT:  CELL_WT);
        }
      }
    }

    /**
     * Updates the payoff value at each (row, col).
     * @param player The player passed to the mapping method.
     * @param host The IBoardModel that is doing the mapping.
     * @param params an int[] of size 2 to return the pay-off value.
     * @param row The the current row that is being processed.
     * @param col The current column that is being processed.
     * @param cellVal The value {-1, 0, +1} found at the above (row, col)
     * where -1 = player #0, 0 = no player, +1 = player #1.
     * @return true
     */
    public boolean apply(int player, IBoardModel host,
                         int row, int col, int cellVal, Integer... params) {
        if (cellVal == 0) {
            return true;
        }

        params[ (cellVal+1 - 2*cellVal*player)/2  ] += payOff(row, col, dim, host);
        params[ 2+((cellVal+1 - 2*cellVal*player)/2)  ]++;  // count tokens

        return true;
    }

    public void noApply(int player, IBoardModel host, Integer... params) {
        /**@todo: Implement this model.IBoardLambda method*/
        throw new java.lang.UnsupportedOperationException("Method noApply() not yet implemented.");
    }

    private final boolean corners(int row, int col, Dimension dim){
        return (row == 0 && col == 0) ||
               (row == 0 && col == dim.width - 1) ||
               (row == dim.height - 1 && col == 0) ||
               (row == dim.height - 1 && col == dim.width - 1);
    }

    private final int badCorner(int row, int col, Dimension dim, IBoardModel host){

      if(0 == host.playerAt(0,0))
      {
        if(row == 0 && col == 1)return SIDE_CORNER_FACTOR;
        else if(row == 1)
        {
          if(col == 0) return SIDE_CORNER_FACTOR;
          else if (col==1) return OFF_CORNER_FACTOR;
        }
      }
      else if(0 == host.playerAt(0,dim.width - 1))
      {
        if(row==0 && col == dim.width - 1) return SIDE_CORNER_FACTOR;
        else if(row == 1)
        {
          if(col == dim.width-1) return SIDE_CORNER_FACTOR;
          else if(col == dim.width-2) return OFF_CORNER_FACTOR;
        }
      }
      else if(0 == host.playerAt(dim.height - 1,0))
      {
        if(row == dim.height-1 && col ==1) return SIDE_CORNER_FACTOR;
        else if(row == dim.height - 2)
        {
          if(col == 0) return SIDE_CORNER_FACTOR;
          else if(col == 1) return OFF_CORNER_FACTOR;
        }
      }
      else if(0 == host.playerAt(dim.height - 1,dim.width - 1))
      {
        if(row == dim.height-1 && col ==dim.width-2) return SIDE_CORNER_FACTOR;
        else if(row == dim.height - 2)
        {
          if(col == dim.width-1) return SIDE_CORNER_FACTOR;
          else if(col == dim.width-2) return OFF_CORNER_FACTOR;
        }
      }
      return 1;
   }

    private final boolean borders(int row, int col, Dimension dim){
        return row == 0 || col == 0 ||
               col == dim.width - 1 || row == dim.height - 1;
    }

    private final int payOff(int row,int col, Dimension dim, IBoardModel host) {
        return badCorner(row, col, dim, host)*cellWts[row][col];
    }
}
