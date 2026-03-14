public class Map {
    private char[][] grid;
    private int width;
    private int height;

    public Map(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new char[height][width];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                grid[r][c] = '.';
            }
        }
    }

    public char getTile(int row, int col) {
        return grid[row][col];
    }

    public void setTile(int row, int col, char tile) {
        grid[row][col] = tile;
    }

    public boolean isInBounds(int row, int col) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    public boolean isPassable(int row, int col) {
        if (!isInBounds(row, col)) return false;
        char tile = grid[row][col];
        return tile != '#';
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public String render(int p1row, int p1col, int p2row, int p2col) {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int c = 0; c < width; c++) {
            sb.append(c % 10);
        }
        sb.append("\n");

        for (int r = 0; r < height; r++) {
            sb.append(String.format("%2d", r));
            for (int c = 0; c < width; c++) {
                if (r == p1row && c == p1col) {
                    sb.append('1');
                } else if (r == p2row && c == p2col) {
                    sb.append('2');
                } else {
                    sb.append(grid[r][c]);
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static Map createDefaultMap() {
        Map map = new Map(12, 12);
        String[] layout = {
            "############",
            "#..........#",
            "#.##.#####.#",
            "#.#........#",
            "#.#.####.#.#",
            "#...#..#.#.#",
            "#.###..#.#.#",
            "#.#....#...#",
            "#.#.####.#.#",
            "#..........#",
            "#.######..E#",
            "############"
        };
        for (int r = 0; r < 12; r++) {
            for (int c = 0; c < 12; c++) {
                map.setTile(r, c, layout[r].charAt(c));
            }
        }
        return map;
    }
}
