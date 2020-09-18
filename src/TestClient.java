
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;



public class TestClient {

	public static void main(String[] args) {
		
//		List<Double> list = new ArrayList<Double>();
//		for(double i=100;i>0;i=i-10)
//			list.add(i);
//		
////		list.sort(Comparator.comparingDouble(m -> m));
//		System.out.println(list);
		try {
			System.out.println(InetAddress.getLoopbackAddress().getHostAddress().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
