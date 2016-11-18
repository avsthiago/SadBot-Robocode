package aps_robocode;
import robocode.*;
import java.awt.Color;


public class SadBot extends Robot
{
	//Posições nas quais o robô se movimentará
	public double[][] pos = new double[12][2];
	//Primaira posição do vetor pos que o robo se movimentará
	int posMenorDist = 0;
	
	public void run() 
	{
		//Personaliza as cores do robô e atribui as posições de movimentação do robô
		 atribuiCores();
		 atribuiValPosicoes();
		
		//Encontra a menor distância entre o robô e os pontos de movimentação
		for(int i = 1; i < 12; i++)
		{
			double distAtual = distanciaPontos(getX(), getY(), pos[i][0], pos[i][1]);
			double menDistAtual = distanciaPontos(getX(), getY(), pos[posMenorDist][0], pos[posMenorDist][1]);
			if(distAtual < menDistAtual)
				posMenorDist = i;
		}
		
		//Se move para a menor distância
		irPara(pos[posMenorDist][0], pos[posMenorDist][1]);

		// Robot main loop
		while(true) 
		{
			//Move o robô para todos os pontos no sentido anti horário
			for(; posMenorDist < 12; posMenorDist++)
			{
				turnGunRight(360);
				irPara(pos[posMenorDist][0], pos[posMenorDist][1]);
			}
			posMenorDist = 0;
		}
	}
	
	//Personaliza as cores do robô
	private void atribuiCores()
	{
		setBodyColor(Color.black);
		setGunColor(Color.black);	
		setRadarColor(Color.red);
		setScanColor(Color.white);
		setBulletColor(Color.red);
	}
	
	//Insere no vetor de posições todas as posições que o robô pode se movimentar
	private void atribuiValPosicoes()
	{
		pos[0][0]  = 230; pos[0][1]  = 500;
		pos[1][0]  = 255; pos[1][1]  = 310;
		pos[2][0]  = 311; pos[2][1]  = 250;
		pos[3][0]  = 500; pos[3][1]  = 230;
		pos[4][0]  = 690; pos[4][1]  = 250;
		pos[5][0]  = 747; pos[5][1]  = 310;
		pos[6][0]  = 770; pos[6][1]  = 500;
		pos[7][0]  = 747; pos[7][1]  = 690;
		pos[8][0]  = 690; pos[8][1]  = 750;
		pos[9][0]  = 500; pos[9][1]  = 770;
		pos[10][0] = 311; pos[10][1] = 750;
		pos[11][0] = 230; pos[11][1] = 692;
	}

	//Calcula a distância euclidiana entre dois pontos
	private double distanciaPontos(double x1,double y1,double x2,double y2)
	{
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
	}
	
	//Roraciona o robô e move ele de sua posição atual para uma informada por parâmetro
	
	//Foi baseado nesse código http://old.robowiki.net/robowiki?Movement/CodeSnippetBasicGoTo
	private void irPara(double x, double y) 
	{
		double rotAbsolutaRad = Math.atan2(x - getX(), y - getY());
		double angulo = rotAbsolutaRad - Math.toRadians(getHeading());
		double normalRelAngRad = Math.atan2(Math.sin(angulo), Math.cos(angulo));
	
        turnRight(Math.toDegrees(normalRelAngRad));	
        ahead(distanciaPontos(getX(), getY(), x, y));
    }
	
	//Calcula a força que o projetil será disparado de acordo com a distância do alvo
	double forcaTiro(double distancia)
	{
		double	forca = 3 - (3 * distancia/1000);
		return forca < 0.1 ? 0.1 : forca;
	}

	/*
		Sempre que um robô for escaneado, é verificado se ele não é o SentryRobot
		e se a temperatura do canão é 0. Se ambas as condições forem verdadeiras ele disparará
	*/
	public void onScannedRobot(ScannedRobotEvent e) 
	{	
		if(!e.isSentryRobot() && getGunHeat() == 0)
			fire(forcaTiro(e.getDistance()));
	}

	/*
		Quando atingido ele gira o canhão para seu oponente e espera que o 
		evento onScannedRobot decida se ele atirará ou não.
	*/
	
	//Baseado no evento onScannedRobot desse site http://www.robocodebrasil.com.br/uploads/6iIjtWXNnv-38-11.java
	public void onHitByBullet(HitByBulletEvent e) 
	{
		turnGunRight(getHeading() - getGunHeading() + e.getBearing());		
	}

	/*
		Quando bater em um adverário ele gira o canhão para seu oponente e espera que o 
		evento onScannedRobot decida se ele atirará ou não.
	*/
	
	//Baseado no evento onScannedRobot desse site http://www.robocodebrasil.com.br/uploads/6iIjtWXNnv-38-11.java
	public void onHitRobot(HitRobotEvent e)
	{
		turnGunRight(getHeading() - getGunHeading() + e.getBearing());	
	}	
}
