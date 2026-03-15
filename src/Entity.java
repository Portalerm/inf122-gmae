public class Entity extends VirtualEntity {
    private int row;
    private int col;
    
    public Entity(int row, int col, int hp, int attackPower) {
        super(hp, hp, attackPower);
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    } 
}
