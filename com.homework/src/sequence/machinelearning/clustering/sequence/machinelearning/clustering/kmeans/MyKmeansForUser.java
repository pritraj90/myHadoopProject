package sequence.machinelearning.clustering.kmeans;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import sequence.machinelearning.clustering.canopy.UserPoint;



public class MyKmeansForUser {

	static Vector<UserPoint>  li=new Vector<UserPoint>();
	//static List<UserPoint>  li=new ArrayList<UserPoint>();
	static List<Vector<UserPoint>> list=new ArrayList<Vector<UserPoint>>(); //每次迭代保存结果，一个vector代表一个簇
	private final static Integer K=3; //选K=2，也就是估算有两个簇。
	private final static Double converge=0.01; //当距离小于某个值的时候，就认为聚类已经聚类了，不需要再迭代，这里的值选0.001	
	
	//读取数据
	public static final void readF1() throws IOException {      
		String filePath="datafile/cluster/data.csv";
		BufferedReader br = new BufferedReader(new InputStreamReader(
        new FileInputStream(filePath)));
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            if(line.length()==0||"".equals(line))continue;
        	String[] str=line.split(",");               
            UserPoint p0=new UserPoint();
            p0.setUserid(str[0]);
    		p0.setX(Double.valueOf(str[1]));
    		p0.setY(Double.valueOf(str[2]));
    		li.add(p0);
            //System.out.println(line);               
        }
        br.close();
    }
	  //math.sqrt(double n)
    //扩展下，如果要给m开n次方就用java.lang.StrictMath.pow(m,1.0/n);
	//采用欧氏距离
	public static  Double DistanceMeasure(UserPoint p1,UserPoint p2){
		
		Double tmp=StrictMath.pow(p2.getX()-p1.getX(), 2)+StrictMath.pow(p2.getY()-p1.getY(), 2);
		return Math.sqrt(tmp);
	}
	
	//计算新的簇心
	public static Double CalCentroid(){
		System.out.println("------------------------------------------------");
		Double movedist=Double.MAX_VALUE;
		for(int i=0;i<list.size();i++){
			Vector<UserPoint> subli=list.get(i);
			UserPoint po=new UserPoint();
			Double sumX=0.0;
			Double sumY=0.0;
			Double Clusterlen=Double.valueOf(subli.size());
			for(int j=0;j<Clusterlen;j++){
				UserPoint nextp=subli.get(j);
				sumX=sumX+nextp.getX();
				sumY=sumY+nextp.getY();
			}
			po.setX(sumX/Clusterlen);
			po.setY(sumY/Clusterlen);
			//新的点与旧点之间的距离
			Double dist=DistanceMeasure(subli.get(0),po);
			//在多个簇心移动的过程中，返回移动距离最小的值
			if(dist<movedist)movedist=dist;
			list.get(i).clear();
			list.get(i).add(po);
			System.out.println("C"+i+"的簇心为："+po.getX()+","+po.getY()+"用户ID："+po.getUserid());
		}
		String test="ll";
		return movedist;
	}
	//本次的簇心
	//下一次移动的簇心
	
	private static Double move=Double.MAX_VALUE;//移动距离
	//不断地迭代，直到收敛
	public static void RecursionKluster(){
		for(int times=2;move>converge;times++){
			System.out.println("第"+times+"次迭代");
			//默认每一个list里的Vector第0个元素是质心
			for(int i=0;i<li.size();i++){
				UserPoint p=new UserPoint();
				 p=li.get(i);
				int index = -1;
				
	            double neardist = Double.MAX_VALUE;
				for(int k=0;k<K;k++){
					UserPoint centre=list.get(k).get(0);
					double currentdist=DistanceMeasure(p,centre);
					if(currentdist<neardist){
						neardist=currentdist;
						index=k;
					}
				}
				
				System.out.println("C"+index+":的点为："+p.getX()+","+p.getY()+"----用户ID："+p.getUserid());
				list.get(index).add(p);
				
			}
			//重新计算簇心,并返回移动的距离，最小的那个距离
			
			move=CalCentroid();
			System.out.println("本次簇心移动距离为，move="+move);
		}
	}
	
	public static void Kluster(){
		
		for(int k=0;k<K;k++){
			Vector<UserPoint> vect=new Vector<UserPoint>();
			UserPoint p=new UserPoint();
			p=li.get(k);
			vect.add(p);
			list.add(vect);
		}
		System.out.println("第1次迭代");
		//默认每一个list里的Vector第0个元素是质心
		for(int i=K;i<li.size();i++){
			UserPoint p=new UserPoint();
			 p=li.get(i);
			int index = -1;
			
            double neardist = Double.MAX_VALUE;
			for(int k=0;k<K;k++){
				UserPoint centre=list.get(k).get(0);
				double currentdist=DistanceMeasure(p,centre);
				if(currentdist<neardist){
					neardist=currentdist;
					index=k;
				}
			}
			
			System.out.println("C"+index+":的点为："+p.getX()+","+p.getY()+"----用户ID："+p.getUserid());
			list.get(index).add(p);
			
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//读取数据
		readF1();
		//第一次迭代
		Kluster();
		//第一次迭代后计算簇心
		CalCentroid();
		//不断迭代，直到收敛
		RecursionKluster();
	}

}

