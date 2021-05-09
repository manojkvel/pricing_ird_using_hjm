/**
 * 
 */
package cqfportal.shared;



/**
 * @author Workstation
 * 
 */
public class MatrixBean {
	int noOfRows;
	int noOfCols;

	public int getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}

	public int getNoOfCols() {
		return noOfCols;
	}

	public void setNoOfCols(int noOfCols) {
		this.noOfCols = noOfCols;
	}

	public String[] getRowHeader() {
		return rowHeader;
	}

	public void setRowHeader(String[] rowHeader) {
		this.rowHeader = rowHeader;
	}

	public String[] getColHeader() {
		return colHeader;
	}

	public void setColHeader(String[] colHeader) {
		this.colHeader = colHeader;
	}

	String rowHeader[];
	String colHeader[];

	Double[][] values;

	public Double[][] getValues() {
		return values;
	}

	public void setValues(Double[][] values) {
		this.values = values;
	}

	public MatrixBean(int x, int y) {
		noOfRows = x;
		noOfCols = y;
		values = new Double[x][y];
	}

	public MatrixBean(int x, int y, String[] rH, String[] cH) {
		noOfRows = x;
		noOfCols = y;
		rowHeader = rH;
		colHeader = cH;
		values = new Double[x][y];
	}

	public void setValueAt(int x, int y, Double value) {
		values[x][y] = value;
	}

	public Double getValueFrom(int x, int y) {
		return values[x][y];
	}

	public Double[] getRow(int x) {
		return values[x];
	}

	public Double[] getColumn(int y) {
		Double[] cols = new Double[values[0].length];
		for (int i = 0; i < values[0].length; i++) {
			cols[i] = values[i][y];
		}
		return cols;
	}

	public void printMatrix() {
		for (int j = 0; j < noOfRows; j++) {
			for (int i = 0; i < noOfCols; i++) {
				//System.out.print(values[j][i] + ",");
			}
			//System.out.println();

		}
	}
	

}
