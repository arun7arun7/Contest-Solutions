package ds.dsu;

import java.util.HashMap;
import java.util.Map;

/**
 * Dynamic DSU
 *
 */
public class dsu {

	private Map<Integer,Integer> parent;
	private Map<Integer,Integer> size;

	public dsu() {
		parent = new HashMap<>();
		size = new HashMap<>();
	}

	public dsu(int n) {
		parent = new HashMap<>();
		size = new HashMap<>();
		for (int i = 0; i < n; i++) {
			parent.put(i, i);
			size.put(i,1);
		}
	}
	
	void make_set(int v)
	{
		parent.put(v,v);
		size.put(v, 1);
	}
	
	int find_set(int v)
	{
		if(v == parent.get(v))
			return v;
		int k = find_set(parent.get(v));
		parent.put(v,k);
		return k;
		//return parent.put(v, find_set(parent.get(v)));
	}
	
	void union_sets(int a, int b)
	{
		a = find_set(a);
		b = find_set(b);
		if(a != b)
		{
			if(size.get(a) < size.get(b))
			{
				int temp = a;
				a = b;
				b = temp;
			}
			parent.put(b, a);
			size.put(a, size.get(a)+size.get(b));
			//System.out.println("Union : "+a+" "+size.get(a));
		}
	}

	boolean isSameSet(int v1, int v2) {
		return find_set(v1) == find_set(v2);
	}

}
