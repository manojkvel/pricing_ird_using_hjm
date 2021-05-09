package cqfportal.server;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.commons.math3.stat.regression.GLSMultipleLinearRegression;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.ujmp.core.exceptions.MatrixException;

import cqfportal.shared.HJMFactorData;
import cqfportal.shared.IntRate;
import cqfportal.shared.PCAModel;

import Jama.Matrix;
import Jama.QRDecomposition;

public class MultipleLinearRegression {
   

   

    public  PCAModel calibrateCurveFit(PCAModel model,HJMFactorData inputs) throws MatrixException, FileNotFoundException, IOException {
    	
		double[] t0,t1,t2;
		t0=new double[model.getNoOfCols()];
		t1=new double[model.getNoOfCols()];
		t2=new double[model.getNoOfCols()];
		for(int i=0;i<model.getNoOfCols();i++){
			t0[i]=(Double.parseDouble(model.getHeaders()[i]));
			t1[i]=t0[i]*t0[i];
			t2[i]=t0[i]*t1[i];
		}
		double[][] pvs = model.getProminentEVs();
		double[] sqrLambbda = {Math.sqrt(model.getProminentLamdas()[0]),Math.sqrt(model.getProminentLamdas()[1]),Math.sqrt(model.getProminentLamdas()[2])};
		for(int i=0;i<3;i++){
			for(int j=0;j<model.getNoOfCols();j++){
				
					pvs[i][j]*=sqrLambbda[i];
					//System.out.print(pvs[i][j]+" ");
					
			}//System.out.println();
			
		}
	
		//Calculate Vol1  coeffs
		
		double v1_b0 = new Median().evaluate(pvs[0]);
		//System.out.println(v1_b0);
		
		//Calculate Vol2 coeffs
		
        double[][] x = new double[model.getNoOfCols()][3];
        
        for(int i=0;i<model.getNoOfCols();i++){
        	x[i][0]=t0[i];
        	x[i][1]=t1[i];
        	x[i][2]=t2[i]; 
        	}
        double[] y =  pvs[1];
        double[] fittedVol = new double[y.length];
        //System.out.println(t0.length+"=="+y.length);
        
       // GLSMultipleLinearRegression gls = new GLSMultipleLinearRegression();
        OLSMultipleLinearRegression ols = new OLSMultipleLinearRegression();
        ols.newSampleData(y, x);
        
//        for(int i=0;i<ols.estimateRegressionParameters().length;i++)
        //System.out.println(ols.estimateRegressionParameters()[i]);
        
        double v2_b0=ols.estimateRegressionParameters()[0];
        double v2_b1=ols.estimateRegressionParameters()[1];
        double v2_b2=ols.estimateRegressionParameters()[2];
        double v2_b3=ols.estimateRegressionParameters()[3];
        //System.out.println("Fitted Vol");
        for(int i=0;i<y.length;i++){
        	fittedVol[i]= v2_b3*t2[i]+v2_b2*t1[i]+v2_b1*t0[i]+v2_b0;
        	//System.out.println(fittedVol[i]);
        }
        

      //	
      		//Calculate Vol3 coeffs
      		
              x = new double[model.getNoOfCols()][3];
              
              for(int i=0;i<model.getNoOfCols();i++){
              	x[i][0]=t0[i];
              	x[i][1]=t1[i];
              	x[i][2]=t2[i]; 
              	}
              y =  pvs[2];
              fittedVol = new double[y.length];
              //System.out.println(t0.length+"=="+y.length);
              
             // GLSMultipleLinearRegression gls = new GLSMultipleLinearRegression();
              ols = new OLSMultipleLinearRegression();
              ols.newSampleData(y, x);
              
//              for(int i=0;i<ols.estimateRegressionParameters().length;i++)
              //System.out.println(ols.estimateRegressionParameters()[i]);
              
              double v3_b0=ols.estimateRegressionParameters()[0];
              double v3_b1=ols.estimateRegressionParameters()[1];
              double v3_b2=ols.estimateRegressionParameters()[2];
              double v3_b3=ols.estimateRegressionParameters()[3];
              //System.out.println("Fitted Vol");
              for(int i=0;i<y.length;i++){
              	fittedVol[i]= v3_b3*t2[i]+v3_b2*t1[i]+v3_b1*t0[i]+v3_b0;
              	//System.out.println(fittedVol[i]);
              }
              
              
      		
      		//vol principal components
      		inputs.vol1Constant = v1_b0;
      		
      		inputs.vol2Constant = v2_b0;
      		inputs.vol2CoeffOfT = v2_b1;
      		inputs.vol2CoeffOfTSquare = v2_b2;
      		inputs.vol2CoeffOfTCube = v2_b3;
      		
      		inputs.vol3Constant = v3_b0;
      		inputs.vol3CoeffOfT = v3_b1;
      		inputs.vol3CoeffOfTSquare = v3_b2;
      		inputs.vol3CoeffOfTCube = v3_b3;
      		
      		model.setInputs(inputs);
        
        return model;
//        MultipleLinearRegression regression = new MultipleLinearRegression(x, y);
//
//        //System.out.printf("%.14f + %.14f beta1 + %.14f beta2  + %.14f beta2   (R^2 = %.14f)\n",
//                      regression.beta(0), regression.beta(1), regression.beta(2), regression.beta(3), regression.R2());
    }
}
