package com.solarSystem;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Color;

class Controller{
        
    public static Display display;
    public static ImageView picture;
    public static TextView debugOutput;
    public static Spinner loader;
    public static ArrayAdapter<String> selection;
    public static Button resetButton;
    public static Button centerButton;
    public static Button focusButton;
    public static AssetManager assetManager;
    
//     public static void initialize(){
//         
//         display = getWindowManager().getDefaultDisplay();
//         picture = (ImageView)findViewById(R.id.picture);
//         debugOutput = (TextView)findViewById(R.id.debugOutput);
//         resetButton = (Button)findViewById(R.id.reset);
//         assetManager = getAssets();
//     }
    
}

class OfUse{
    public static double maximum = Math.pow(10,6);
    public static double minimum = Math.pow(10,-12);
    public static double angleFactor = Math.PI * 0.3 / Math.sqrt(2.) ;
    public static double pixelThreshold = 5. ;
    public static double angleThreshold = Math.PI / 180. ;
    public static double pointsONcircle = 360 ;
    public static double pointsONsphere = 1000 ;

    public static double zufaellig(double unter, double ober){
        return zufaellig( unter, ober, 1 ) ;
    }

    public static double zufaellig(double unter, double ober, int vert){
        double rueck = 0.;
        if(vert<1) rueck=(ober-unter) * 0.5;
        else{
            for(int i=0; i<vert; i++){
                rueck += unter + Math.random() * (ober - unter);
            }
            rueck /= vert;
        }
        return rueck;
    }
    
    public static double[] sphere(){
        double[] rueck = new double[]{ zufaellig(-1.,1.) , zufaellig(-1.,1.) , zufaellig(-1.,1.) };
        double norm = Math.sqrt( rueck[0] * rueck[0] + rueck[1] * rueck[1] + rueck[2] * rueck[2] );
        rueck[0] /= norm ;
        rueck[1] /= norm ;
        rueck[2] /= norm ;
        return rueck ;
    }
    
}

class Dot{
    double[] koord;
    int col;
    public Dot(double a, double b, int c){
        koord = new double[2];
        koord[0] = a;
        koord[1] = b;
        col = c;
    }
    public String toString(){
        return " Dot ( "+this.koord[0]+" , "+this.koord[1]+" ) mit Farbe : "+col;
    }
} 

class Punkt{
    double[] koord;
    int col;
    public Punkt(){
        koord = OfUse.sphere();
        col = Color.WHITE;
    }
    public Punkt(double a, double b, double c, int d){
        koord = new double[3];
        koord[0] = a;
        koord[1] = b;
        koord[2] = c;
        col = d;
    }
    public Punkt(double[] ueb, int d){
        koord = new double[3];
        for(int i=0; i<3; i++){
            koord[i] = ueb[i];
        }
        col = d;
    }
    public Punkt(Punkt pu){
        this.koord = new double[3];
        for(int i=0; i<3; i++){
            this.koord[i] = pu.koord[i];
        }
        this.col = pu.col;
    }
    public void mult(double mal){
        for(int i=0; i<3; i++){
            this.koord[i] = mal*this.koord[i];
        }
    }
    public Punkt multNew(double mal){
        Punkt rueck = new Punkt( this.koord , this.col ) ;
        for(int i=0; i<3; i++){
            rueck.koord[i] = mal*this.koord[i];
        }
        return rueck;
    }
    public Punkt adi(Punkt pu){
        Punkt rueck = new Punkt(1.,1.,1.,0);
        for(int i=0; i<3; i++){
            rueck.koord[i] = this.koord[i] + pu.koord[i];
        }
        return rueck;
    }
    public Punkt subt(Punkt pu){
        Punkt rueck = new Punkt(1.,1.,1.,0);
        for(int i=0; i<3; i++){
            rueck.koord[i] = this.koord[i] - pu.koord[i];
        }
        return rueck;
    }
    public double prod(Punkt pu){
        double rueck=0.;
        for(int i=0; i<3; i++){
            rueck += this.koord[i]*pu.koord[i];
        }
        return rueck;
    }
    
    public Punkt cross(Punkt pu){
        Punkt rueck = new Punkt(1.,1.,1.,0);
        rueck.col = this.col * pu.col;
        rueck.koord[0] = this.koord[1]*pu.koord[2] - this.koord[2]*pu.koord[1];
        rueck.koord[1] = this.koord[2]*pu.koord[0] - this.koord[0]*pu.koord[2];
        rueck.koord[2] = this.koord[0]*pu.koord[1] - this.koord[1]*pu.koord[0];
        return rueck;
    }
    public double norm(){
        double rueck=Math.sqrt(this.prod(this));
        return rueck;
    }
    public Punkt normal(){
        Punkt rueck = new Punkt( this.koord , this.col ) ;
        double norm = this.norm();
        if(norm>OfUse.minimum){
            rueck.mult(1./norm);
        }
        return rueck;
    }
    public double dist(Punkt pu){
        Punkt arbeiter = this.subt(pu);
        double rueck = arbeiter.norm();
        return rueck;
    }
    public double winkel(Punkt pu){
        double rueck=0.;
        rueck = Math.acos( this.prod(pu)/this.norm()/pu.norm() );
        return rueck;
    }
    public String toString(){
        return " Punkt ( "+this.koord[0]+" , "+this.koord[1]+" , "+this.koord[2]+" ) mit Farbe : "+col;
    }
}

class Matrix{
    int zeile;
    int spalte;
    double[][] mat;
    public Matrix(int a, int b){
        this.zeile = a;
        this.spalte = b;
        this.mat = new double[a][b]; 
    }
    public Matrix(Punkt pu, double wi){
        this.zeile = 3;
        this.spalte = 3;
        double ca = Math.cos(wi);
        double sa = Math.sin(wi);
        Punkt npu = pu.normal();
        this.mat = new double[3][3];
        //
        this.mat[0][0] = pu.koord[0]*pu.koord[0]*(1.-ca)+ca;
        this.mat[0][1] = pu.koord[0]*pu.koord[1]*(1.-ca)-pu.koord[2]*sa;
        this.mat[0][2] = pu.koord[0]*pu.koord[2]*(1.-ca)+pu.koord[1]*sa;
        //
        this.mat[1][0] = pu.koord[1]*pu.koord[0]*(1.-ca)+pu.koord[2]*sa;
        this.mat[1][1] = pu.koord[1]*pu.koord[1]*(1.-ca)+ca;
        this.mat[1][2] = pu.koord[1]*pu.koord[2]*(1.-ca)-pu.koord[0]*sa;
        //
        this.mat[2][0] = pu.koord[2]*pu.koord[0]*(1.-ca)-pu.koord[1]*sa;
        this.mat[2][1] = pu.koord[2]*pu.koord[1]*(1.-ca)+pu.koord[0]*sa;
        this.mat[2][2] = pu.koord[2]*pu.koord[2]*(1.-ca)+ca;
    }
    public Matrix(double[][] emat){
        this.zeile = emat.length;
        this.spalte = emat[0].length;
        for(int i=1; i<this.spalte; i++){
            if(emat[i].length<this.spalte){this.spalte=emat[i].length;}
        }
        this.mat = new double[this.zeile][this.spalte];
        for(int i=0; i<this.zeile; i++){
            for(int j=0; j<this.spalte; j++){
                this.mat[i][j]=emat[i][j];
            }
        }
    }
    public void ein(int a, int b, double c){
        this.mat[a][b] = c;
    }
//     public void ausgabe(){
//         for(int i=0; i<this.mat.length; i++){
//             System.out.print(" [ ");
//             for(int j=0; j<this.mat[i].length; j++){
//                 System.out.printf("%s %8.4f %s"," ",mat[i][j]," ");
//             }
//             System.out.println(" ]");
//         }
//     }
    public Matrix mult(Matrix mal){
        Matrix rueck = new Matrix(this.zeile, mal.spalte);
        if(this.spalte==mal.zeile){
            for(int i=0; i<this.zeile; i++){
                for(int j=0; j<mal.spalte; j++){
                    double sum=0.;
                    for(int k=0; k<this.spalte; k++){
                        sum += this.mat[i][k] * mal.mat[k][j];
                    }
                    //System.out.print(sum+" ");
                    rueck.ein( i, j, sum);
                }
            }
            //System.out.println(" ");
        }
        else{
//             System.out.println(" Dimensionalitaeten passen nicht! ");
            for(int i=0; i<this.zeile; i++){
                for(int j=0; j<mal.spalte; j++){
                    if(i==j){
                        rueck.ein( i, j, 1.);
                    }
                    else{
                        rueck.ein( i, j, 0.);
                    }
                }
            }
        }
        return rueck;
    }
    public Dot mulDtoD(Dot doot){
        Dot rueck = new Dot(1., 1., 0);
        rueck.col = doot.col;
        if(this.zeile==2 && this.spalte==2){
            for(int i=0; i<2; i++){
                rueck.koord[i]=0;
                for(int j=0; j<2; j++){
                    rueck.koord[i] += this.mat[i][j] * doot.koord[j];
                }
            }
        }
//         else{
//             System.out.println(" Dimensionalitaeten passen nicht! ");
//         }
        return rueck;
    }
    public Punkt mulPtoP(Punkt pu){
        Punkt rueck = new Punkt( 1., 1., 1., 0);
        rueck.col = pu.col;
        if(this.zeile==3 && this.spalte==3){
            for(int i=0; i<3; i++){
                rueck.koord[i]=0;
                for(int j=0; j<3; j++){
                    rueck.koord[i] += this.mat[i][j] * pu.koord[j];
                }
            }
        }
//         else{
//             System.out.println(" Dimensionalitaeten passen nicht! ");
//         }
        return rueck;
    }
    public Dot mulPtoD(Punkt pu){
        Dot rueck = new Dot(1., 1., 0);
        rueck.col = pu.col;
        if(this.zeile==2 && this.spalte==3){
            for(int i=0; i<2; i++){
                rueck.koord[i]=0;
                for(int j=0; j<3; j++){
                    rueck.koord[i] += this.mat[i][j] * pu.koord[j];
                }
            }
        }
//         else{
//             System.out.println(" Dimensionalitaeten passen nicht! ");
//         }
        return rueck;
    }
    public String toString(){
        return " Zeilen : "+this.zeile+" ; Spalten : "+this.spalte;
    }
}

class RotMat extends Matrix{
    int achse;
    double winkel;
    //static super.zeile = 3;
    //static super.spalte = 3;
    public RotMat(int a, double p){
        super(3,3);
        achse = a%3;
        winkel = p%(2*Math.PI);
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                if(i==achse){
                    if(j==achse){
                        super.mat[i][j]=1.;
                    }
                    else{
                        super.mat[i][j]=0.;
                    }
                }
                else if(j==achse){
                    super.mat[i][j]=0.;
                }
                else if(i==j){
                    super.mat[i][j]=Math.cos(winkel);
                }
                else if(i>j){
                    if(achse==1){super.mat[i][j]=Math.sin(winkel);}
                    else{super.mat[i][j]=-Math.sin(winkel);}
                }
                else{
                    if(achse==1){super.mat[i][j]=-Math.sin(winkel);}
                    else{super.mat[i][j]=Math.sin(winkel);}
                }
            }
        }
    }
    public String toString(){
        String ax;
        if(this.achse==0){
            ax = "x";
        }
        if(this.achse==1){
            ax = "y";
        }
        if(this.achse==2){
            ax = "z";
        }
        else{
            ax = "unbekannte";
        }
        double wingra = this.winkel*180/2/Math.PI;
        return " Rotationsmatrix um "+ax+"-Achse um "+wingra+" Grad ";
    }
}

class Massive{
    double mass;
    Punkt position;
    Punkt velocity;
    double radius = 0.;
    double orbit = 0.;
    public Massive(double px,double py,double pz,double vx,double vy, double vz,double m,int color,double r){
        this.mass = m;
        this.radius = r;
        this.position = new Punkt( px , py , pz , color );
        this.velocity = new Punkt( vx , vy , vz , color );
    }
    public Massive(double px,double py,double pz,double vx,double vy, double vz,double m,int color){
        this( px , py , pz , vx , vy , vz , m , color , 0. );
    }
    public Massive(double px,double py,double pz,double vx,double vy, double vz,double m){
        this( px , py , pz , vx , vy , vz , m , Color.WHITE );
    }
    public Massive(Punkt p,Punkt v,double m, double r){
        this.mass = m;
        this.radius = r;
        this.position = new Punkt( p );
        this.velocity = new Punkt( v );
    }
    public Massive(Punkt p,Punkt v,double m){
        this( p , v , m , 0. );
    }
    public Massive( Massive m ){
        this.mass = m.mass ;
        this.radius = m.radius ;
        this.orbit = m.orbit ;
        this.position = new Punkt( m.position );
        this.velocity = new Punkt( m.velocity );
    }
    public void orbits( Massive central ){
        this.orbit = this.position.subt( central.position ).norm();
    }
}

class database{
    
    public static ArrayList< Massive > masses = new ArrayList<Massive>();

    public static ArrayList< Punkt > orte = new ArrayList<Punkt>();
    public static ArrayList< Punkt > zBuffer = new ArrayList<Punkt>();
    public static ArrayList< Dot > zweiD = new ArrayList<Dot>();
    
    public static Punkt central = new Punkt( 0. , 0. , 0. , 0 );
    public static Punkt mainAxis = new Punkt( 0. , 1. , 0. , 0 );
    public static Punkt secondAxis = new Punkt( 1. , 0. , 0. , 0 );
    public static double[][] extrema = new double[][] {
                                                        { -1. , 1. } ,
                                                        { -1. , 1. } ,
                                                        { -1. , 1. }
                                                    };
    public static double maxRange ;
    
    public static ArrayList< Punkt > currentTouch = new ArrayList<Punkt>();
    public static ArrayList< Punkt > lastTouch = new ArrayList<Punkt>();
    
    public static int[] imageSize;
    public static int imageScale;
    public static double imageDiagonal;
    public static Bitmap projection ;
    public static Bitmap emptyImage ;
    
    public static void fill(){
        fill("galaxy");
    }
    
    public static void fill(String assetFileName){
        readFile( assetFileName );
        evaluateRead();
        initializeBitmaps();
    }
    
    public static void readFile(String assetFileName){

        Integer counter = 0;
        
        String[] files = new String[1];
        String line;
        String[] columns;
        
        masses = new ArrayList<Massive>();
        orte = new ArrayList< Punkt >();
        
        try{
            files = Controller.assetManager.list("");
            counter = files.length;
        }
        catch(IOException e){
            Controller.debugOutput.setText(
                " can not read database "
            );
        }
        
        try{
        
            BufferedReader reader = new BufferedReader( 
                                        new InputStreamReader( 
                                            Controller.assetManager.open(assetFileName+".txt") 
                                        ) 
                                    );
            
            counter = 0;
            while( ( line = reader.readLine() ) != null ){
            
                String[] columnsT = line.split("\t");
                String[] columnsS = line.split(" ");
                String[] columnsC = line.split(",");
                String[] columnsH = line.split(";");
            
                columns = columnsT;
                if( columns.length < columnsS.length ) columns = columnsS ;
                if( columns.length < columnsC.length ) columns = columnsC ;
                if( columns.length < columnsH.length ) columns = columnsH ;
                
                if( columns.length == 1 ){
                    try{
                        Double number = Double.parseDouble( columns[0] );
                        orte.add( new Punkt(
                                number ,
                                counter ,
                                0. ,
                                Color.WHITE
                            ) 
                        );
                    }
                    catch (NumberFormatException ex) {
                       continue;
                    }
                }
                else if( columns.length == 2 ){
                    orte.add( new Punkt(
                            Double.parseDouble( columns[0] ) ,
                            Double.parseDouble( columns[1] ) ,
                            0. ,
                            Color.WHITE
                        ) 
                    );
                }
                else if( columns.length == 3 ){
                    orte.add( new Punkt(
                            Double.parseDouble( columns[0] ) ,
                            Double.parseDouble( columns[1] ) ,
                            Double.parseDouble( columns[2] ) ,
                            Color.WHITE
                        ) 
                    );
                }
                else if( columns.length == 4 ){
                    orte.add( new Punkt(
                            Double.parseDouble( columns[0] ) ,
                            Double.parseDouble( columns[1] ) ,
                            Double.parseDouble( columns[2] ) ,
                            Integer.parseInt(   columns[3] )
                        ) 
                    );
                }
                else if( columns.length == 7 ){
                    masses.add( new Massive(
                            Double.parseDouble( columns[0] ) ,
                            Double.parseDouble( columns[1] ) ,
                            Double.parseDouble( columns[2] ) ,
                            Double.parseDouble( columns[3] ) ,
                            Double.parseDouble( columns[4] ) ,
                            Double.parseDouble( columns[5] ) ,
                            Double.parseDouble( columns[6] ) 
                        ) 
                    );
                }
                else if( columns.length == 9 ){
                    masses.add( new Massive(
                            Double.parseDouble( columns[0] ) ,
                            Double.parseDouble( columns[1] ) ,
                            Double.parseDouble( columns[2] ) ,
                            Double.parseDouble( columns[3] ) ,
                            Double.parseDouble( columns[4] ) ,
                            Double.parseDouble( columns[5] ) ,
                            Double.parseDouble( columns[6] ) ,
                            Integer.parseInt(   columns[8] ) ,
                            Double.parseDouble( columns[7] ) 
                        ) 
                    );
                }
                else if( columns.length > 4 ){
                    orte.add( new Punkt(
                            Double.parseDouble( columns[0] ) ,
                            Double.parseDouble( columns[1] ) ,
                            Double.parseDouble( columns[2] ) ,
                            Color.WHITE
                        ) 
                    );
                }
                else continue ;
                
                counter++;
                
            }
            
        }
        catch(IOException e){
            line = "" ;
            for(int i=0; i<counter; i++){
                line += files[i];
                line += "\n";
            }
            Controller.debugOutput.setText(
                " can not read database "
                +
                " #files : "
                +
                counter.toString()
                +
                "\n"
                +
                line
            );
        }
        
    }
    
    public static void evaluateRead(){
    
        boolean[] filled = new boolean[]{ false , false };
        boolean fullMean = true ;
        
        central = new Punkt( 0. , 0. , 0. , 0 );
        
        Massive origin = new Massive( central , central , 0. );
        int largestAttractor = -1 ;
        
        if( orte.size() > 0 ) filled[0] = true ;
    
        if( masses.size() > 0 ){
        
            filled[1] = true ;
            
            for(int m=0; m<masses.size(); m++){
                
                orte.add( masses.get(m).position ) ;
                
                if( masses.get(m).radius == 0. ) continue ;
                
                for(int p=0; p<OfUse.pointsONsphere; p++){
                    Punkt onSphere = new Punkt( OfUse.sphere() , masses.get(m).position.col );
                    onSphere.mult( masses.get(m).radius );
                    onSphere = onSphere.adi( masses.get(m).position );
                    onSphere.col = masses.get(m).position.col ;
                    orte.add( onSphere );
                }
                
                if( masses.get(m).mass > origin.mass ){
                    largestAttractor = m ;
                    origin = new Massive( masses.get(m) ) ;
                }
                
            }
            
            if( largestAttractor == 0 ){
                
                for(int m=1; m<masses.size(); m++){
                    
                    if( masses.get(m).radius == 0. ) continue ;
                    
                    masses.get(m).orbits( origin ) ;
                    
                    if( masses.get(m).orbit == 0. ) continue ;
                    
                    Punkt connection = masses.get(m).position.subt( origin.position ).normal();
                    Punkt tangent = connection.cross( masses.get(m).velocity.normal() );
                    tangent = tangent.cross( connection ).normal();
                    
                    for(int p=0; p<OfUse.pointsONcircle; p++){
                        double angle = p * OfUse.angleThreshold ;
                        Punkt onCircle = tangent.multNew( Math.cos( angle ) )
                                                .adi( connection.multNew( Math.sin( angle ) ) )
                                                .multNew( masses.get(m).orbit )
                                                .adi( origin.position );
                        onCircle.col = masses.get(m).position.col ;
                        orte.add( onCircle );
                    }
                    
                }
                
            }
            
        }
        
        for(int i=0; i<orte.size(); i++){
           
            if( i < 1 ){
            
                extrema = new double[][] {
                    { orte.get(i).koord[0] , orte.get(i).koord[0] } ,
                    { orte.get(i).koord[1] , orte.get(i).koord[1] } ,
                    { orte.get(i).koord[2] , orte.get(i).koord[2] }
                };
                
            }
            else{
            
                for(int c=0 ; c<3 ; c++ ){
                
                    if( extrema[c][0] > orte.get(i).koord[c] )
                        extrema[c][0] = orte.get(i).koord[c] ;
                    
                    if( extrema[c][1] < orte.get(i).koord[c] )
                        extrema[c][1] = orte.get(i).koord[c] ;
                        
                }
                
            }
            
            if( largestAttractor != 0 ) central = central.adi( orte.get(i) );
        
        }
            
        if( largestAttractor != 0 ) central.mult( 1. / orte.size() );
        else central = new Punkt( origin.position ); 
        
        if( 
            extrema[0][1] != extrema[0][0]
            ||
            extrema[1][1] != extrema[1][0]
            ||
            extrema[2][1] != extrema[2][0]
        ){
        
            int[] maxAxis = new int[]{ 0 , 1 };
            int[][] other = new int[][]{
                { 1 , 2 } ,
                { 0 , 2 } ,
                { 0 , 1 }
            };
            
            double[] differences = new double[]{
                extrema[0][1] - extrema[0][0] ,
                extrema[1][1] - extrema[1][0] ,
                extrema[2][1] - extrema[2][0]
            };

            if( largestAttractor == 0 ){
            
                for(int c=0; c<3; c++){
                    differences[c] = 2. * Math.max(
                        Math.abs( extrema[c][0] - central.koord[c] )
                        ,
                        Math.abs( extrema[c][1] - central.koord[c] )
                    ) ;
                }
            
            }
                
            for(int c=0; c<3; c++){
                if( maxRange < differences[c] || c < 1 ){
                    maxRange = differences[c] ;
                    maxAxis[0] = c ;
                }
            }   
            
            if( differences[other[maxAxis[0]][0]] > differences[other[maxAxis[0]][1]] ) 
                maxAxis[1] = other[maxAxis[0]][0] ; 
            else 
                maxAxis[1] = other[maxAxis[0]][1] ; 
                
            Punkt[] basis = new Punkt[]{
                new Punkt( 1. , 0. , 0. , 0 ) ,
                new Punkt( 0. , 1. , 0. , 0 ) ,
                new Punkt( 0. , 0. , 1. , 0 ) 
            };
            
            mainAxis   = new Punkt( basis[ maxAxis[0] ] );
            secondAxis = new Punkt( basis[ maxAxis[1] ] );
            
        }
        else{
            maxRange = 1. ;
            mainAxis   = new Punkt( 1. , 0. , 0. , 0 );
            secondAxis = new Punkt( 0. , 1. , 0. , 0 );
        }
        
    }
        
    public static void initializeBitmaps(){
                    
        Point dot = new Point();
        Controller.display.getSize(dot);
        imageSize = new int[]{ (int)( (double)dot.x / 0.8 ) , dot.y };
        
//         imageSize = new int[]{
//                                 Controller.picture.getMeasuredWidth() ,
//                                 Controller.picture.getMeasuredHeight() 
//                             };
        
        imageDiagonal = Math.sqrt( imageSize[0] * imageSize[0] + imageSize[1] * imageSize[1] );
                                        
        imageScale = imageSize[0] ;
        if( imageScale > imageSize[1] ) 
            imageScale = imageSize[1] ;               
            
//         Controller.debugOutput.setText(
//                                 " width "
//                                 +
//                                 imageSize[0]
//                                 +
//                                 " | "
//                                 +
//                                 " height "
//                                 +
//                                 imageSize[1]
//                             );
                            
        projection = Bitmap.createBitmap( 
                                            imageSize[0], 
                                            imageSize[1], 
                                            Bitmap.Config.ARGB_8888
                                        );    
                                        
        emptyImage = projection.copy( projection.getConfig() , true ) ;
        
        for(int c=0; c<imageSize[0]; c++){
            emptyImage.setPixel( c , 0 , Color.GRAY );
            emptyImage.setPixel( c , imageSize[1]-1 , Color.GRAY );
        }
        for(int c=0; c<imageSize[1]; c++){
            emptyImage.setPixel( 0 , c , Color.GRAY );
            emptyImage.setPixel( imageSize[0]-1 , c , Color.GRAY );
        }
        
    }
    
    static void currentTOlast(){
        lastTouch.clear() ;
        for(int t=0; t<currentTouch.size(); t++){
            lastTouch.add( new Punkt(
                currentTouch.get(t).koord[0] ,
                currentTouch.get(t).koord[1] ,
                currentTouch.get(t).koord[2] ,
                currentTouch.get(t).col
            ) );
        }
    }
    
    static void lastTOcurrent(){
        currentTouch.clear() ;
        for(int t=0; t<lastTouch.size(); t++){
            currentTouch.add( new Punkt(
                lastTouch.get(t).koord[0] ,
                lastTouch.get(t).koord[1] ,
                lastTouch.get(t).koord[2] ,
                lastTouch.get(t).col
            ) );
        }
    }
    
    static void toText(){
        Controller.debugOutput.setText("");
        for(int p=0; p<currentTouch.size(); p++){
            Controller.debugOutput.setText(
                Controller.debugOutput.getText()
                +"\n"+
                " current "+currentTouch.get(p).col+" at "
                +"("
                    +(int)currentTouch.get(p).koord[0]
                    +"|"
                    +(int)currentTouch.get(p).koord[1]
                +")"
            );
        }
        for(int p=0; p<lastTouch.size(); p++){
            Controller.debugOutput.setText(
                Controller.debugOutput.getText()
                +"\n"+
                " last "+lastTouch.get(p).col+" at "
                +"("
                    +(int)lastTouch.get(p).koord[0]
                    +"|"
                    +(int)lastTouch.get(p).koord[1]
                +")"
            );
        }
    }
    
}

class Projektor{
    static double bereich ;
    static Punkt beob ;
    static Punkt forward ;
    static Punkt upDir ;
    public static void start(double ber){
        bereich = ber ;
        beob    = new Punkt(0., 0., ber, 0);
        forward = new Punkt(0., 0., -1, 0);
        upDir   = new Punkt(0., 1., 0., 0);
    }
    public static void start(double ber, Punkt be, Punkt fow, Punkt up){
        bereich = ber ;
        beob = new Punkt( be );
        forward = new Punkt( fow );
        upDir = new Punkt( up );
    }
    public static void startBYdata(){
        bereich = database.maxRange / 3. ;
        upDir = database.mainAxis;
        forward = database.secondAxis.cross( database.mainAxis ).multNew(-1.).normal();
        beob = database.central.adi( forward.multNew( - database.maxRange / 3. ) );
    }
    public static void projektion(){

        ///////////////////////////////define variables/////////////////////////////////////////////
        double rx, ry, rz;
        int rc;
        double near = bereich / 100.;
        double far = 3. * bereich ;
        double oefwin = 120.*Math.PI/180./2.;
        double ow = 0.;
        double setver = 1.;
        double sv = 0.;
        Punkt zax = new Punkt(forward);
        zax = zax.multNew(-1.).normal();
        Punkt yax = new Punkt(upDir);
        yax = yax.normal();
        Punkt xax = yax.cross(zax).normal();
        Matrix camTrafo = new Matrix(4,4);
        Matrix projMat = new Matrix(4,4);
        double[] homCoord = new double[4];
        double[] projHom = new double[4];
        double win=0.;
        double distance=0.;
//         double wver=Math.cos(oefwin);
        double wver=0.;
        ///////////////////////////////define variables END/////////////////////////////////////////////

        if( Math.abs(Math.tan(oefwin)) > OfUse.minimum ){ow=1./Math.tan(oefwin);}
        else{ow=OfUse.maximum;}
        if( Math.abs(Math.tan(setver)) > OfUse.minimum ){sv=1./Math.tan(setver);}
        else{sv=OfUse.maximum;}

        ///////////////////////////////calculate trafomatrices////////////////////////////////
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                //Kameratransformationsmatrix
                if(i==0){
                    if(j<3){camTrafo.mat[i][j]=xax.koord[j];}
                    else{camTrafo.mat[i][j]=-xax.prod(beob);}
                }
                else if(i==1){
                    if(j<3){camTrafo.mat[i][j]=yax.koord[j];}
                    else{camTrafo.mat[i][j]=-yax.prod(beob);}
                }
                else if(i==2){
                    if(j<3){camTrafo.mat[i][j]=zax.koord[j];}
                    else{camTrafo.mat[i][j]=-zax.prod(beob);}
                }
                else{
                    if(j<3){camTrafo.mat[i][j]=0.;}
                    else{camTrafo.mat[i][j]=1.;}
                }
                //Projektionsmatrix
                if(i==0 && j==0){projMat.mat[i][j]=sv;}
                else if(i==1 && j==1){projMat.mat[i][j]=ow;}
                else if(i==2 && j==2){projMat.mat[i][j]=far/(near-far);}
                else if(i==2 && j==3){projMat.mat[i][j]=near*far/(near-far);}
                else if(i==3 && j==2){projMat.mat[i][j]=-1.;}
                else{projMat.mat[i][j]=0.;}
                /*
                if(i==j){projMat.mat[i][j]=1.;}
                else{projMat.mat[i][j]=0.;}
                */
            }
        }
        projMat = projMat.mult(camTrafo);
        ///////////////////////////////calculate trafomatrices END////////////////////////////////

        //////////////////////////////projekt orte to zBuffer///////////////////////////////
        database.zBuffer.clear();
        for(int k=0; k<database.orte.size(); k++){
//             win = forward.winkel( database.orte.get(k).subt( beob ) );
            distance = database.orte.get(k).dist( beob );
//             if( ( distance >= near && distance <= far ) && win >= wver ){
            if( distance >= near ){
                //Kamtrafo und Projektion
                for(int i=0; i<4; i++){
                    if(i<3){homCoord[i]=database.orte.get(k).koord[i];}
                    else{homCoord[i]=1.;}
                }
                for(int i=0; i<projHom.length; i++){
                    projHom[i]=0.;
                    for(int j=0; j<projMat.mat[i].length; j++){
                        projHom[i] += projMat.mat[i][j] * homCoord[j];
                    }   
                }
                rc = database.orte.get(k).col;
                database.zBuffer.add(new Punkt(projHom, rc));
            }
        }
        //////////////////////////////projekt orte to zBuffer END///////////////////////////////
    }
    public static void zBuffering(){
        database.zweiD.clear();
        double rx, ry, cz;
        int rc;
        int buffer;
        int num = database.zBuffer.size();
        int newnum = 0;
        int order[] = new int[num];
//         double values[] = new double[num];
//         for(int i=1; i<num; i++){
//             order[i] = i;
//             values[i] = database.zBuffer.get(i).koord[2];
//         }
//         ////////////////////////////////bubble sort//////////////////////////////////
//         while(num!=0){
//             newnum = 0;
//             for(int i=1; i<num; i++){
//                 if( values[order[i]] > values[order[i-1]] ){
//                     buffer = order[i];
//                     order[i] = order[i-1];
//                     order[i-1] = buffer;
//                     newnum = i;
//                 }
//                 /*
//                 if(database.zBuffer.get(i-1).koord[2] > database.zBuffer.get(i).koord[2]){
//                     buffer = database.zBuffer.get(i);
//                     database.zBuffer.get(i) = database.zBuffer.get(i-1);
//                     database.zBuffer.get(i-1) = buffer;
//                     newnum = i;
//                 }
//                 */
//             }
//             num = newnum;
//         }
//         ////////////////////////////////bubble sort END//////////////////////////////////
        num = database.zBuffer.size();
        for(int i=num-1; i>=0; i--){
            order[i] = i ;
            cz = database.zBuffer.get(order[i]).koord[2];
            if(cz > OfUse.minimum){
                rx = database.zBuffer.get(order[i]).koord[0] / cz;
                ry = database.zBuffer.get(order[i]).koord[1] / cz;
            }
            else{
                rx = OfUse.maximum;
                ry = OfUse.maximum;
            }
            rc = database.zBuffer.get(order[i]).col;
            database.zweiD.add( new Dot(rx ,ry, rc));
        }
    }
    public static void draw(){
        database.projection = database.emptyImage.copy( database.emptyImage.getConfig() , true ) ;
        double tx, ty;
        double ber = 1.;
        int gx, gy;
        for(int k=database.zweiD.size()-1; k>0; k--){
        
            tx = database.zweiD.get(k).koord[0];
            ty = database.zweiD.get(k).koord[1];    
//             if(Math.abs(tx)<ber && Math.abs(ty)<ber){
            gx = (int)( 
                        ( 
                            database.imageScale * ( ber - tx ) 
                            + database.imageSize[0] - database.imageScale 
                        ) * 0.5 
                    ); 
            gy = (int)( 
                        ( 
                            database.imageScale * ( ber - ty ) 
                            + database.imageSize[1] - database.imageScale 
                        ) * 0.5 
                    );
//                 if( gy < 0 || gy >= database.imageSize[1] ) continue ;
            if( 
                gx < 0 || gx >= database.imageSize[0]
                ||
                gy < 0 || gy >= database.imageSize[1]
            )
                continue ;
                
            database.projection.setPixel( gx , gy , database.zweiD.get(k).col );
//             }
        }
    }
    public static void initialize(){
        projektion();
        zBuffering();
        draw();
    }
    public static void rotateView(Punkt rotAx, double angle){
        if( rotAx.norm() < OfUse.minimum || Math.abs( angle ) < OfUse.minimum ) return ;
        Matrix rotMat = new Matrix(rotAx, angle);
        if( forward.cross( rotAx ).norm() > OfUse.minimum ){
            forward = rotMat.mulPtoP(forward);
            forward = forward.normal();
        }
        if( upDir.cross( rotAx ).norm() > OfUse.minimum ){
            upDir = rotMat.mulPtoP(upDir);
            upDir = upDir.normal();
        }
        initialize();
    }
    public static void rotatePos(Punkt rotAx, double angle){
        if( rotAx.norm() < OfUse.minimum || Math.abs( angle ) < OfUse.minimum ) return ;
        Matrix rotMat = new Matrix(rotAx, angle);
        beob = beob.adi( forward.multNew( bereich ) ) ;
//         beob = rotMat.mulPtoP(beob);
        if( forward.cross( rotAx ).norm() > OfUse.minimum ){
            forward = rotMat.mulPtoP(forward);
            forward = forward.normal();
        }
        if( upDir.cross( rotAx ).norm() > OfUse.minimum ){
            upDir = rotMat.mulPtoP(upDir);
            upDir = upDir.normal();
        }
        beob = beob.subt( forward.multNew( bereich ) ) ;
        initialize();
    }
    public static void movePos(Punkt direction, double stepsize){
        Punkt step = new Punkt(direction);
        step = step.normal().multNew(stepsize);
        beob = beob.adi(step);
        initialize();
    }
    public static void singleInput(){

        double[] scale = new double[]{ 
                                database.currentTouch.get(0).koord[0] - database.lastTouch.get(0).koord[0] ,
                                database.currentTouch.get(0).koord[1] - database.lastTouch.get(0).koord[1]
                            } ;
        
        if( 
            Math.abs( scale[0] ) < OfUse.pixelThreshold 
            && 
            Math.abs( scale[1] ) < OfUse.pixelThreshold 
        )
            return ;
            
        if( Math.abs( scale[0] ) < OfUse.pixelThreshold * 0.5 ) scale[0] = 0 ;
        if( Math.abs( scale[1] ) < OfUse.pixelThreshold * 0.5 ) scale[1] = 0 ;
            
        Punkt axis = forward.cross( upDir ).normal() ;
        Punkt direction = upDir.multNew( scale[1] ).adi( axis.multNew( scale[0] ) ).normal();
        axis = direction.cross( forward );
        double ratio = Math.sqrt( scale[0] * scale[0] + scale[1] * scale[1] ) / database.imageScale ;
        rotatePos( axis , ratio * OfUse.angleFactor );
        
    }
    public static boolean multiInput(){
    
        Punkt currentDirection = database.currentTouch.get(0).subt( database.currentTouch.get(1) ) ;
        Punkt lastDirection = database.lastTouch.get(0).subt( database.lastTouch.get(1) ) ;
        
        double[] distances = new double[]{ 
            currentDirection.norm() ,
            lastDirection.norm() 
        } ;
        
        double rotation = currentDirection.winkel( lastDirection ) ;
        
        Punkt currentMean = database.currentTouch.get(0).adi( database.currentTouch.get(1) ).multNew(0.5) ;
        Punkt lastMean = database.lastTouch.get(0).adi( database.lastTouch.get(1) ).multNew(0.5) ;
        
        Punkt moved = currentMean.subt( lastMean ) ;
        
        if( Math.abs( distances[0] - distances[1] ) > OfUse.pixelThreshold ){
        
            double scaling = ( distances[0] - distances[1] ) / distances[1] ;
//             double scaling = ( distances[0] - distances[1] ) / database.imageDiagonal ;
            
            beob = beob.adi( forward.multNew( bereich * scaling ) ); 
            bereich *= ( 1. - scaling ) ;
            initialize() ;
            
        }
        else if( moved.norm() > OfUse.pixelThreshold ){
            
            Punkt toMove = upDir.multNew( moved.koord[1] )
                                .adi( upDir.cross( forward ).multNew( -moved.koord[0] ) ) ;
            toMove.mult( bereich / database.imageDiagonal ) ;
            beob = beob.adi( toMove ) ;
            initialize() ;
            
        }
        else if( Math.abs( rotation ) > OfUse.angleThreshold ){

            Punkt axis = currentDirection.cross( lastDirection );
            if( axis.koord[2] > 0. ) rotation *= -1. ;
            
            rotateView( forward , rotation ) ;
            
        }
        else return false ;
        
        return true ;
                            
    }
    public static void toText(){
                    
        Controller.debugOutput.setText(
            String.format(
                            " - "+
                            "%1$.2f"+
                            "\n"+
                            " + ("+
                                        "%2$.2f"+"|"+
                                        "%3$.2f"+"|"+
                                        "%4$.2f"+
                                    ")"+
                            "\n"+
                            " > ("+
                                        "%5$.2f"+"|"+
                                        "%6$.2f"+"|"+
                                        "%7$.2f"+
                                    ")"+
                            "\n"+
                            " ^ ("+
                                        "%8$.2f"+"|"+
                                        "%9$.2f"+"|"+
                                        "%10$.2f"+
                                    ")"
                            ,
                            bereich ,
                            beob.koord[0] / bereich , beob.koord[1] / bereich , beob.koord[2] / bereich ,
                            forward.koord[0], forward.koord[1], forward.koord[2],
                            upDir.koord[0]  , upDir.koord[1]  , upDir.koord[2]
                        )
        );  
                
    }
}

public class MainActivity extends Activity {

    static int identification , position , index ;
    static boolean toDraw , overwrite ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
//         Controller.initialize();
        
        Controller.display = getWindowManager().getDefaultDisplay();
        Controller.picture = (ImageView)findViewById(R.id.picture);
        Controller.debugOutput = (TextView)findViewById(R.id.debugOutput);
        Controller.loader = (Spinner)findViewById(R.id.loader); 
        Controller.resetButton = (Button)findViewById(R.id.reset);
        Controller.centerButton = (Button)findViewById(R.id.center);
        Controller.focusButton = (Button)findViewById(R.id.focus);
        Controller.assetManager = getAssets();
        
        Controller.selection = new ArrayAdapter<String>( 
            this , android.R.layout.simple_spinner_dropdown_item ,
            new ArrayList<String>( Arrays.asList( "galaxy" , "planets" , "sphere" , "colorful" ) )
        );
        
        Controller.loader.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View v, int position, long id) {  
                database.fill( Controller.selection.getItem(position).toString() );
                Projektor.startBYdata();
                Projektor.initialize() ;
                Controller.picture.setImageBitmap( database.projection );
                Projektor.toText() ;
            }
            @Override
            public void onNothingSelected(AdapterView parent) {
                Controller.loader.setSelection( Controller.selection.getPosition( "galaxy" ) );
            }
        });
        Controller.loader.setAdapter( Controller.selection );
        
        database.fill();
        Projektor.startBYdata();
        Projektor.initialize() ;
        Controller.picture.setImageBitmap( database.projection );
        Projektor.toText() ;
        
        Controller.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            
                Projektor.startBYdata();
                Projektor.initialize() ;
                Controller.picture.setImageBitmap( database.projection );
                Projektor.toText() ;
                
            }
        });
        
        Controller.centerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            
                Projektor.beob = database.central.subt( Projektor.forward.multNew( Projektor.bereich ) );
                Projektor.initialize() ;
                Controller.picture.setImageBitmap( database.projection );
                Projektor.toText() ;
                
            }
        });
        
        Controller.focusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                Punkt direction = database.central.subt( Projektor.beob ) ;
                Projektor.bereich = direction.norm();
                direction = direction.normal() ;
                Projektor.rotateView( 
                    Projektor.forward.cross( direction ) , 
                    Projektor.forward.winkel( direction )
                ) ;
                Controller.picture.setImageBitmap( database.projection );
                Projektor.toText() ;
                
            }
        });
        
        Controller.picture.setOnTouchListener( new View.OnTouchListener(){
    
            @Override
            public boolean onTouch(View v, MotionEvent event){
            
                toDraw = false ;
                overwrite = true ;
                    
                database.currentTouch.clear() ;
                    
                if( event.getPointerCount() == 1 ){
                
                    database.currentTouch.add( new Punkt(
                        event.getX() ,
                        event.getY() ,
                        0. , 
                        event.getPointerId(0)
                    ) );
                    
                    switch( event.getAction() ){
                    
                        case MotionEvent.ACTION_DOWN:
                            break;
                            
                        case MotionEvent.ACTION_MOVE:
                            Projektor.singleInput();
                            toDraw = true;
                            break;
                            
                        case MotionEvent.ACTION_UP:
                            Projektor.singleInput();
                            toDraw = true;
                            break;
                            
                    }
                
                }
                else{
                
                    if( database.lastTouch.size() < 2 && event.getPointerCount() == 2 ){
                    
                        for( index=0 ; index<2 ; index++ ){
                            database.currentTouch.add( new Punkt(
                                event.getX(index) ,
                                event.getY(index) ,
                                0. ,
                                event.getPointerId(index)
                            ) ) ;
                        }
                        database.currentTOlast() ;
                        
                    }
                    else{
                    
                        database.lastTOcurrent() ;
                        for( index=0 ; index<event.getPointerCount() ; index++ ){
                            position = 2 ;
                            identification = event.getPointerId(index) ;
                            if( database.lastTouch.get(0).col == identification ) position = 0 ;
                            else if( database.lastTouch.get(1).col == identification ) position = 1 ;
                            if( position < 2 ){
                                database.currentTouch.set( position , new Punkt(
                                    event.getX(index) ,
                                    event.getY(index) ,
                                    0. ,
                                    identification
                                ) ) ;
                            }
                        }
                        
                    }
                    
                    switch( event.getAction() ){
                    
                        case MotionEvent.ACTION_POINTER_DOWN:
                            break;
                            
                        case MotionEvent.ACTION_MOVE:
                            overwrite = Projektor.multiInput() ;
                            toDraw = true ;
                            break;
                            
                        case MotionEvent.ACTION_POINTER_UP:
                
                            index = event.getActionIndex() ;
                            identification = event.getPointerId( index ) ;
                        
                            position = 2 ;
                            if( database.lastTouch.get(0).col == identification ) position = 0 ;
                            else if( database.lastTouch.get(1).col == identification ) position = 1 ;
                            
                            if( position < 2 ){
                                database.lastTouch.remove(position) ;
                                database.currentTouch.remove(position) ;
                            }
                            
                            break;
                            
                    }
                    
                }
                
                    Projektor.toText();
//                     database.toText();
                
                if( toDraw ) Controller.picture.setImageBitmap( database.projection );
                if( overwrite ) database.currentTOlast() ;
                    
                return true;
                    
            }
            
        } );
        
    }
}
