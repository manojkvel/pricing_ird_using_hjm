/**
 * 
 */
package cqfportal.server;

/**
 * @author Workstation
 *
 */
import java.awt.Paint;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.util.PaintUtilities;
import org.ujmp.core.Matrix;
import org.ujmp.core.MatrixFactory;
import org.ujmp.core.calculation.Calculation.Ret;
import org.ujmp.core.enums.FileFormat;
import org.ujmp.core.exceptions.MatrixException;
import org.ujmp.core.matrix.DenseMatrix;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cqfportal.client.apps.finalproject.PCAService;
import cqfportal.shared.HJMFactorData;
import cqfportal.shared.IntRate;
import cqfportal.shared.PCAModel;


public class PCAServiceImpl extends RemoteServiceServlet implements PCAService {
	
	private String fileName="";
	
	public HJMFactorData getPCAResults(){
		return null;
		//pca();
		//"C:\\Learning\\CQF\\Project\\YeildCurveData.csv"
	}
	
	public static void main(String args[]) throws MatrixException, FileNotFoundException, IOException{
		PCAServiceImpl pca =new PCAServiceImpl();
		pca.pca("C:\\Learning\\CQF\\Project\\YeildCurveData1.csv");
	}
	
	public PCAModel pca(String fileName) throws MatrixException, FileNotFoundException, IOException{
		this.fileName = fileName;
    	Matrix forwardRateData = MatrixFactory.importFromStream(FileFormat.CSV, new FileInputStream(fileName), ",");
    	
    	//print(forwardRateData);
        forwardRateData = forwardRateData.toDoubleMatrix();
        int nor = Long.valueOf(forwardRateData.getRowCount()).intValue();
        double[][] fwdArr = forwardRateData.toDoubleArray();
        
        double[] tenor0 = fwdArr[nor-1];
       
        
        
        /**
         * Calculate the forwateRate daily differences
         **/
        forwardRateData.deleteRows(Ret.NEW, 0).divide(100.0);
        Matrix forwardRates = forwardRateData;
        printToFile(forwardRateData, "C:\\Learning\\CQF\\Project\\YeildCurveStart.txt");
        Matrix diffs = diff(forwardRates);
        printToFile(diffs, "C:\\Learning\\CQF\\Project\\YeildCurveDiffer.txt");
       // Matrix sample = MatrixFactory.copyFromMatrix(diffs);
       // //System.out.println(diffs.toString());
        /**
         * Normalize the diff data w.r.t mean for the sample
         **/
       // double[][] meanReducedReturns = sample.toDoubleArray();
       
        //Matrix covarianceMatrix = MatrixFactory.importFromArray(covXY(ROW,COL,meanNormalize(sample.toDoubleArray())));
        
        
        /**
         * Generate the covariance matrix for the normalized data; scale it to 252/10000 because of the yield curve data in percents
         **/
        
        
       
        double scale = 252/10000d;
       Matrix covarianceMatrix =  diffs.cov(Ret.NEW, true).times(scale);
        		//diffs.transpose().mtimes(diffs);
        printToFile(covarianceMatrix, "C:\\Learning\\CQF\\Project\\YeildCurveCov.txt");
        //print(covarianceMatrix);
        
        
        
        
//    Matrix m = Matrix.
        //System.out.println("Forward Rates");
      //  //System.out.println(forwardRates.toString());
    
    //System.out.println("DIfferences");
    ////System.out.println(diffs.toString());
    //Annualized, using 252 business days...
    //System.out.println("COvariance Matrix");
    
    ////System.out.println(covarianceMatrix.toString());
    //System.out.println("Eigen Vectors");
   PCAModel pcaModel = orderedEigenData(covarianceMatrix);
   pcaModel.setNoOfRows(pcaModel.getCovMatrix().length);
   pcaModel.setNoOfCols(pcaModel.getCovMatrix().length);
    //System.out.println("XXXX");
//    MatrixGUIObject gui = new MatrixGUIObject(MatrixFactory.importFromArray(pcaModel.getProminentEVs()));
    CategoryDataset dataset = DatasetUtilities.createCategoryDataset("PCA", "", pcaModel.getProminentEVs()); 
    JFreeChart jfree = ChartFactory.createLineChart("Principal Component Analysis", "Maturity", "Eigen Vector", dataset, PlotOrientation.VERTICAL, true, false, false);
    LegendItemCollection legend = new LegendItemCollection();
    Paint[] colors= {PaintUtilities.stringToColor("BLUE"),PaintUtilities.stringToColor("RED"),PaintUtilities.stringToColor("GREEN")};
    for (int i = 0; i < 3; ++i) {
        jfree.getCategoryPlot().getRenderer().setSeriesPaint(i, colors[i]);
        LegendItem li = new LegendItem("PCA", "-", null, null, Plot.DEFAULT_LEGEND_ITEM_BOX, colors[i]);
        legend.add(li);
    }  
    jfree.getCategoryPlot().setFixedLegendItems(legend);

  
   ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
   //System.out.println(getServletContext().getRealPath("/")+"cqfportal\\PCAChart.JPG");
   ChartUtilities.saveChartAsJPEG(new File(getServletContext().getRealPath("/")+"cqfportal\\PCAChart.JPG"), jfree, 400, 400);
   
   MultipleLinearRegression mlr = new MultipleLinearRegression();
   HJMFactorData inputs = new HJMFactorData();
   for(int i=0;i<pcaModel.getNoOfCols();i++){
	   inputs.day0TermStructure.intRates.add(new IntRate(Double.parseDouble(pcaModel.getHeaders()[i]),tenor0[i]));
   }
   //System.out.println("Tenor0");
   //System.out.println(inputs.day0TermStructure.intRates.toString());
   
//    MatrixChartPanel ch = new MatrixChartPanel();
//    ChartConfiguration config = new ChartConfiguration();
//    ch.export(FileFormat.JPG, new File("C:\\Learning\\CQF\\Project\\PCAChart.JPG"));
    //System.out.println("XXXX");
    inputs.maturities=pcaModel.getHeaders();
    return mlr.calibrateCurveFit(pcaModel, inputs);
 
}
	
	public  double[][] covXY(int row,int col,double[][] sample){
		double[][] cov = new double[col][col];
		for(int k=0;k<col;k++){
			for(int j=0;j<col;j++){
//				//System.out.print("["+k+"]["+j+"]=");
				for(int i=0;i<row;i++){
					cov[k][j]+=sample[i][k]*sample[i][j]*252/10000;
//					 //System.out.print("["+i+"]["+k+"]*["+i+"]["+j+"]+");
				}
//				//System.out.println();
				cov[k][j]=cov[k][j]/(col-1);
			}
			
		}
		return cov;
	}
	
	 public  Matrix diff1(Matrix forwardRates) {
	        //offset the fwd rates by one
	        Matrix offset = DenseMatrix.factory.zeros(1, forwardRates.getColumnCount());
	        offset.fill(Ret.LINK, Double.NaN);
	        offset = offset.appendVertically(forwardRates);
	        offset = offset.deleteRows(Ret.LINK, offset.getRowCount() - 1);
	        Matrix diffs = forwardRates.minus(offset).deleteRows(Ret.NEW, 0);
	        return diffs;
	    }
	 
	 public  Matrix diff(Matrix forwardRates) {
		 double[][] sampledata = forwardRates.toDoubleArray();
		 double[][] sampleDiffata = new double[sampledata.length-1][sampledata[0].length];
		 for(int i=1;i<sampledata.length;i++){
				for(int j=0;j<sampledata[0].length;j++){
					sampleDiffata[i-1][j]=sampledata[i][j] - sampledata[i-1][j];
				}
			}
	        return MatrixFactory.importFromArray(sampleDiffata);
	    }
	 
	 
	 
	
	public  double[][] meanNormalize(double[][] sample){
	int noOfRows = sample.length;
	int noOfcols = sample[0].length;
	double mean[] = new double[noOfcols];
		for( int i = 0; i < noOfRows; i++ ) {
            for( int j = 0; j < noOfcols; j++ ) {
                mean[j] += sample[i][j];
            }
        }
        for( int j = 0; j < noOfcols; j++ ) {
            mean[j] /= noOfRows;
        }

        // subtract the mean from the original data
        for( int i = 0; i < noOfRows; i++ ) {
            for( int j = 0; j < noOfcols; j++ ) {
                sample[i][j]=sample[i][j]-mean[j];
               
            }
        }
        
        return sample;
	}
	
	
	public  void print(Matrix m){
		double[][] md = m.toDoubleArray();
		int r = md.length;
		int c = md[0].length;
		for(int i=0;i<r;i++){
			for(int j=0;j<c;j++){
				//System.out.print(md[i][j]+" ");
			}
			//System.out.println();
		}
		
	}
	
	public  void printToFile(Matrix m,String fileName){
		if(false){
			File file = new File(fileName);
			file.delete();
			try {
		        BufferedWriter out = new BufferedWriter(new FileWriter(fileName), 32768);
		       
		    double[][] md = m.toDoubleArray();
			int r = md.length;
			int c = md[0].length;
			for(int i=0;i<r;i++){
				for(int j=0;j<c;j++){
					out.append((md[i][j]+" "));
				}
				out.append("\r");
			}
			out.close();
			} catch (IOException e) {
		    }
		}
		
		
		
	}
	
	public  void printToFile(double[] m,String fileName){
		if(false){
			File file = new File(fileName);
			file.delete();
			try {
		        BufferedWriter out = new BufferedWriter(new FileWriter(fileName), 32768);
		       
		    int r = m.length;
			for(int i=0;i<r;i++){
					out.append((m[i]+" "));
				out.append("\r");
			}
			out.close();
			} catch (IOException e) {
		    }
			
			
		}
		
	}
	public  void print(double[] m){
	       
	    int r = m.length;
		for(int i=0;i<r;i++){
			System.out.print(m[i]+" ");		}System.out.println();
		
		
	}
	
	public  PCAModel orderedEigenData(Matrix covarianceMatrix) {
		PCAModel pcaModel = new PCAModel();
		pcaModel.setCovMatrix(covarianceMatrix.toDoubleArray());
		 JacobiServiceImpl jacobi = new JacobiServiceImpl();
		    Matrix eigenVectors =jacobi.getEigenVectors(covarianceMatrix.toDoubleArray(), 1E-20 );
		    if(eigenVectors!=null){
		    	pcaModel.setEigenVectors(eigenVectors.toDoubleArray());
		    printToFile(eigenVectors, "C:\\Learning\\CQF\\Project\\YeildCurveEigV.txt");}
	   // Eig.EigMatrix eigenMatrix = new Eig.EigMatrix(covarianceMatrix);
	    
        /**
         * covariance matrix is symmetric, so only real eigenvalues...
         **/
	    double[] eigenValues = jacobi.Eigenvaluesevec(covarianceMatrix.toDoubleArray(), 1E-20);
	    pcaModel.setEigenValues(eigenValues);
	    
	    printToFile(eigenValues, "C:\\Learning\\CQF\\Project\\YeildCurveEigValues.txt");
	    //double[] eigenValues = eigenMatrix.getRealEigenvalues();
	    
	    ////System.out.println("Eigen Values");
        /**
         * Extract the Eigen Vectors from the Eigen Matrix
         **/
	    
	   // Matrix eigenVectors = eigenMatrix.getV();
	   
	    double[][] eigenVMatrix = eigenVectors.toDoubleArray();
	    printToFile(eigenVectors, "C:\\Learning\\CQF\\Project\\YeildCurveEigVArray.txt");
	    int N = eigenVMatrix[0].length;
	    HashMap<Integer,Double> bestfit = new HashMap<Integer, Double>();
	    HashMap<Double,Integer> reverseMap = new HashMap<Double,Integer>();
	    for( int i = 0; i <N; i++ ) {
	    	bestfit.put(i, eigenValues[i]);
	    	reverseMap.put( eigenValues[i],i);
	    }
	    ArrayList<Double> bestFitList = new ArrayList<Double>(bestfit.values());
	    Collections.sort(bestFitList);
	    //System.out.println("************");
	    //System.out.println(bestFitList);
	    //System.out.println("************");
	    //System.out.println(bestfit);
        /**
         * Get the 3 prominent principal components
         **/

//	    //System.out.println(bestFitList.get(bestFitList.size()-1)+","+bestFitList.get(bestFitList.size()-2)+","+bestFitList.get(bestFitList.size()-3));
	    
	    double lambda1 = bestFitList.get(bestFitList.size()-1);
	    double lambda2 = bestFitList.get(bestFitList.size()-2);
	    double lambda3 = bestFitList.get(bestFitList.size()-3);
	    
	    double[] prominentLambdas = {lambda1,lambda2,lambda3};
	    
	    double[] PCA1 = new double[N]; 
	    //System.out.println("************");
	    double[] PCA2 = new double[N];
	    double[] PCA3 = new double[N];
	    for(int i=0;i<N;i++){
	    	if(i==reverseMap.get(lambda1)){
	    		for(int j=0;j<N;j++){
		    	PCA1[j]	= eigenVMatrix[j][i];
		    	}
	    	}else if(i==reverseMap.get(lambda2)){
	    		for(int j=0;j<N;j++){
		    	PCA2[j]	= eigenVMatrix[j][i];
		    	}
	    	}else if(i==reverseMap.get(lambda3)){
	    		for(int j=0;j<N;j++){
		    	PCA3[j]	= eigenVMatrix[j][i];
		    	}
	    	}
	    	
	    }
	   
	    
	   // for(int k=0;k<PCA1.length;k++)
	    {
	    	//System.out.println(reverseMap.get(bestFitList.get(bestFitList.size()-1))+" "+reverseMap.get(bestFitList.get(bestFitList.size()-2))+" "+reverseMap.get(bestFitList.get(bestFitList.size()-3)));
	   	 //System.out.println(bestFitList.get(bestFitList.size()-1)+" "+bestFitList.get(bestFitList.size()-2)+" "+bestFitList.get(bestFitList.size()-3));
	   }
	    //System.out.println();
	    //System.out.println();
	    double[][] prominentEVs = new double[3][eigenVMatrix[0].length];
	    prominentEVs[0]=PCA1;
	    prominentEVs[1]=PCA2;
	    prominentEVs[2]=PCA3;
	    for(int k=0;k<PCA1.length;k++){
	    	 //System.out.println(PCA1[k]+" "+PCA2[k]+" "+PCA3[k]);
	    }
	    pcaModel.setProminentEVs(prominentEVs);
	    pcaModel.setProminentLamdas(prominentLambdas);
	    pcaModel.setHeaders(colHeader);
	    return pcaModel;
	}

	String[] colHeader = { "0.08", "0.50", "1.00", "1.50", "2.00", "2.50",
			"3.00", "3.50", "4.00", "4.50", "5.00", "5.50", "6.00", "6.50",
			"7.00", "7.50", "8.00", "8.50", "9.00", "9.50", "10.00",
			"10.50", "11.00", "11.50", "12.00", "12.50", "13.00", "13.50",
			"14.00", "14.50", "15.00", "15.50", "16.00", "16.50", "17.00",
			"17.50", "18.00", "18.50", "19.00", "19.50", "20.00", "20.50",
			"21.00", "21.50", "22.00", "22.50", "23.00", "23.50", "24.00",
			"24.50", "25.00" };

	@Override
	public PCAModel performPCA(String fileName) {
		// TODO Auto-generated method stub
		PCAModel pcaModel = new PCAModel();
		try {
			//System.out.println(getServletContext().getRealPath("/")+fileName);
			pcaModel =  pca(getServletContext().getRealPath("/")+fileName);
		} catch (MatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pcaModel;
	}
	  
	
	
	
}