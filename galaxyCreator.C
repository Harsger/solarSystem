#include <TROOT.h>
#include <TFile.h>
#include <TString.h>
#include <TMath.h>
#include <TBranch.h>
#include <TTree.h>
#include <TF1.h>
#include <TF2.h>
#include <TH1.h>
#include <TH2.h>
#include <TH3.h>
#include <TGraphErrors.h>
#include <TPad.h>
#include <TApplication.h>  
#include <TKey.h>
#include <TRandom3.h>

#include <vector>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include <cmath>
#include <ctime>

using namespace std;

double phiFunction( double t , double limit , double rounds ){
    return 2 * TMath::Pi() * rounds * t / limit ;
}

double radiusFunction( double t , double limit , double end , double offset = 0. ){
//     double slope = ( end - offset ) / ( 1. - exp(-limit) ) ;
//     double intercept = end - slope ;
//     cout << " s" << slope << " i" << intercept << " exp" << exp(-limit) << endl;
//     return intercept + slope * exp( t - limit ) ;
    double slope = ( end - offset ) / ( limit - 0. ) ;
    double intercept = end - slope * limit ;
    return intercept + slope * t ;
}

void galaxyCreator(){
    
    unsigned int counter = 0;
    
    unsigned int requiredStars = 10000;
    unsigned int nSpiralArms = 6;
    double radius = 100000.;
    double width = 10000.;
    double ratioCenterArms = 0.3;
    double centerRadius = 20000.;
    double massRange[2] = { 0.1 , 2000. };
    unsigned int stepsINspiralArm = 100;
    double numberDecrease = 0.05;
    double radiusDecrease = 0.1;
    
    TGraphErrors * xyplane = new TGraphErrors();
    TGraphErrors * yzplane = new TGraphErrors();
    TH1I * masses = new TH1I("masses","masses",2200,-200.,2000.);
    TRandom * generator = new TRandom();
    
    ofstream output("assets/stars.txt");
    
    unsigned int starsPerArm = 0.9 * requiredStars / (double)nSpiralArms ;
    
    for(unsigned int a=0; a<nSpiralArms; a++){
        
//         cout << " ARM " << a << endl;
        
        for(unsigned int c=0; c<stepsINspiralArm; c++){
            
            double angle = 
                            phiFunction( 
                                            (double)c , 
                                            (double)(stepsINspiralArm-1) , 2./3. 
                                       ) 
                            + 2 * TMath::Pi() 
                            * (double)a 
                            / (double)nSpiralArms ;
                            
            double distance = radiusFunction( (double)c , (double)(stepsINspiralArm-1) , radius );
            
            double clusterPosition[3] = {
                                    distance * cos( angle ) ,
                                    distance * sin( angle ) ,
                                    0.
                                };
                                
            unsigned int starsINcluster = (unsigned int)(
                                                            starsPerArm 
                                                            / (double)stepsINspiralArm 
                                                            / ( 0.5 + 0.5 * numberDecrease )
                                                            *
                                                            ( 
                                                                1. 
                                                                - 
                                                                (double)c 
                                                                / (double)stepsINspiralArm
                                                                * ( 1. - numberDecrease )
                                                            )
                                                        );
                                
            double clusterRadius = (double)(
                                                width 
                                                *
                                                ( 
                                                    1.
                                                    -
                                                    (double)c 
                                                    / (double)stepsINspiralArm
                                                    * ( 1. - radiusDecrease )
                                                )
                                            );
            
//             cout << " cluster " << c << " \t => \t # " << starsINcluster << " \t r " << clusterRadius << endl;
            counter += starsINcluster;
            
//             continue;
                                
            for(unsigned int s=0; s<starsINcluster; s++){
                
                double positions[3] = { 0. , 0. , 0. };
                generator->Sphere( positions[0] , positions[1] , positions[2] , 1. );
                double starClusterDistance = clusterRadius * generator->Uniform() ;
                
                for(unsigned int k=0; k<3; k++)
                    positions[k] = clusterPosition[k] + positions[k] * starClusterDistance ;
            
                xyplane->SetPoint(
                                    xyplane->GetN() ,
                                    positions[0] ,
                                    positions[1]
                );
            
                yzplane->SetPoint(
                                    yzplane->GetN() ,
                                    positions[1] ,
                                    positions[2]
                );
                
                double starMass = generator->Landau(15.,6.);
                
                while( starMass < massRange[0] || starMass > massRange[1] )
                    starMass = generator->Landau(15,6.);
                
//                 cout << " " << starMass;
                masses->Fill(starMass);
                
                output << starMass;
                for(unsigned int k=0; k<3; k++)
                    output << "\t" << positions[k];
                output << endl;
                
            }
            
        }
        
    }
    
    cout << " ## " << counter << endl;
    
//     masses->Draw();
    
//     yzplane->Draw("AP");
//     gPad->Modified();
//     gPad->Update();
//     gPad->WaitPrimitive();
    
//     xyplane->Draw("AP");
//     gPad->Modified();
//     gPad->Update();
//     gPad->WaitPrimitive();
    
}
