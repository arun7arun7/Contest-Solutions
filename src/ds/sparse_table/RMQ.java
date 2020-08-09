package ds.sparse_table;

public class RMQ {
     
	int[] arr;
	int[] log;
	int[][] st;
	int n,k;

	public RMQ(int n, int[] arr) {
		this.n = n;
		log = new int[n+1];
		k = (int)(Math.log(n)/Math.log(2));
		st = new int[n][k+1];
		this.arr = new int[n];
		for (int i = 0; i < n; i++) {
			this.arr[i] = arr[i];
		}
		preprocess();
	}

	void preprocess()
	{
		log[1] = 0;
		for (int i = 2; i <= n; i++)
		    log[i] = log[i/2] + 1;
		for (int i = 0; i < n; i++)
		    st[i][0] = arr[i];
		for (int j = 1; j <= k; j++)
		    for (int i = 0; i + (1 << j) <= n; i++)
		        st[i][j] = Math.min(st[i][j-1], st[i + (1 << (j - 1))][j - 1]);
	}

    /**
	 *
	 * @param L index corresponding to arr(starts from 0)
	 * @param R index corresponding to arr(starts from 0)
	 * @return	minimum value between L and R(inclusive) in O(1) time
	 */
	int rmq(int L, int R)
	{
		int j = log[R - L + 1];
		return Math.min(st[L][j], st[R - (1 << j) + 1][j]);
	}

}
