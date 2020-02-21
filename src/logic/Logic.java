package logic;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.Gson;



public class Logic extends Observable<Logic.Observer> {
	
	public static interface Observer {

		public void onGo();
		public void onResults(Results results);
		public void onReset();
		public void onError(String error);
	}

	public Logic() {
		IOH = new IOHandler();
		Checker = new Checkups();
		Ops = new Operations();
		
		isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");		
		
		//Initialize neural network server
		pb = new ProcessBuilder();
		if (isWindows) {
			pb.command("cmd.exe", "/c", "FLASK_ENV=development FLASK_APP=app.py flask run --port=9924"); //Flask on windows?
		} else {
			pb.command("bash", "-c", "FLASK_ENV=development FLASK_APP=app.py flask run --port=9924");
		}
		
		try {
			p = pb.start();
		} catch (Exception e) {
			e.printStackTrace();
			for (Logic.Observer o : observers) {
				o.onError("Can't start the Flask server");
			}
			return;
		}
	}
	
	public void computeAlpha(int states, int maxTrans, int minTrans, int inputs, int outputs, int depth, boolean spearman) {

		for (Logic.Observer o : observers) {
			o.onGo();
		}

		Results results = new Results();
		double[] as = new double[depth];
		int[] depths = new int[depth];

		for (int i = 0; i < depth; i++) {
			as[i] = neuralNet(states, maxTrans, minTrans, inputs, outputs, i+1, spearman);
			depths[i] = i + 1;
		}
		
		results.setAs(as);
		results.setDepth(depth);
		results.setDepths(depths);
		
		for (Logic.Observer o : observers) {
			o.onResults(results);
		}
	}
	
	public void computeAlpha(File file, int depth, boolean spearman) {

		for (Logic.Observer o : observers) {
			o.onGo();
		}
		
		//Needed variables
		Results results = new Results();
		double[] as = new double[depth];
		int[] depths = new int[depth];
		Graph G;
		
		G = IOH.readGraph(file.toString());

		if (G == null) {
			for (Logic.Observer o : observers) {
				o.onError(file.toString() + ": Failled to load the automaton.");
			}
			return;
		}

		if (!Checker.is_valid(G.getMachine())) {
			for (Logic.Observer o : observers) {
				o.onError(file.toString() + ": Non-valid graph.");
			}
			return;
		}
		
		for (int i = 0; i < depth; i++) {
			as[i] = neuralNet(G.getMachine().getStates().size(), G.getMaxTrans(), G.getMinTrans(),
					G.getMachine().getInputAlphabet().size(), G.getOutputAlphabet().size(), i+1, spearman);
			depths[i] = i + 1;
		}
		
		results.setAs(as);
		results.setDepth(depth);
		results.setDepths(depths);
		
		for (Logic.Observer o : observers) {
			o.onResults(results);
		}
	}
	
	public void computeSqueeziness(File file, int depth, boolean spearman) {

		for (Logic.Observer o : observers) {
			o.onGo();
		}
		
		//Needed variables
		Results results = new Results();
		double[] as = new double[depth];
		int[] depths = new int[depth];
		Graph G;
		
		G = IOH.readGraph(file.toString());

		if (G == null) {
			for (Logic.Observer o : observers) {
				o.onError(file.toString() + ": Failled to load the automaton.");
			}
			return;
		}

		if (!Checker.is_valid(G.getMachine())) {
			for (Logic.Observer o : observers) {
				o.onError(file.toString() + ": Non-valid graph.");
			}
			return;
		}
		
		for (int i = 0; i < depth; i++) {
			as[i] = neuralNet(G.getMachine().getStates().size(), G.getMaxTrans(), G.getMinTrans(),
					G.getMachine().getInputAlphabet().size(), G.getOutputAlphabet().size(), i+1, spearman);
			depths[i] = i + 1;
		}
		
		results.setAs(as);
		results.setDepth(depth);
		results.setDepths(depths);
		results.setSqs(Ops.Squeeziness(G, depth, as));
		
		for (Logic.Observer o : observers) {
			o.onResults(results);
		}
	}
	
	public void computeSqueezinessFull(File file, int depth) {

		for (Logic.Observer o : observers) {
			o.onGo();
		}
		
		//Iteration values
		double[] as = {0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9,
				1, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9,
				2, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9,
				3, 3.1, 3.2, 3.3, 3.4, 3.5, 3.6, 3.7, 3.8, 3.9,
				4, 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 4.7, 4.8, 4.9,
				5};

		//Needed variables
		Results results = new Results();
		Graph G;
		
		G = IOH.readGraph(file.toString());

		if (G == null) {
			for (Logic.Observer o : observers) {
				o.onError(file.toString() + ": Failled to load the automaton.");
			}
			return;
		}

		if (!Checker.is_valid(G.getMachine())) {
			for (Logic.Observer o : observers) {
				o.onError(file.toString() + ": Non-valid graph.");
			}
			return;
		}

		Ops.Squeeziness(G, depth, results, as);
		
		for (Logic.Observer o : observers) {
			o.onResults(results);
		}
	}
	
	private double neuralNet(int states, int maxTrans, int minTrans, int inputs, int outputs, int depth, boolean spearman) {

        double p = 0.0;
        double s = 0.0;

//		// json formatted data
        String json = "{ \"data\": [" + Integer.toString(states) + ", " + Integer.toString(maxTrans) + ", "
        		+ Integer.toString(minTrans) + ", " + Integer.toString(inputs) 
        		+ ", " + Integer.toString(outputs) + ", " + Integer.toString(depth) + "] }";

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
        		.build();

        
        HttpRequest request = HttpRequest.newBuilder()
        		.uri(URI.create("http://localhost:9924/predict"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(json))
                .build();
        
        try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

			AlphaDTO obj = new Gson().fromJson(response.body(), AlphaDTO.class);
	        p = obj.pearson_alpha;
	        s = obj.spearman_alpha;
		} catch (IOException e) {
			e.printStackTrace();
			for (Logic.Observer o : observers) {
				o.onError("Failled to call the Flask server. The alpha value is not valid.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			for (Logic.Observer o : observers) {
				o.onError("Failled to call the Flask server. The alpha value is not valid.");
			}
		}
		
        if (spearman) {
        	return s >= 0? s : 0;
        } else {
    		return p >= 0? p : 0;
        }
	}
	
	public void reset() {
		
		for (Logic.Observer o : observers) {
			o.onReset();
		}
	}
	
	public void close() {

		p.children().close();
		p.destroy();
	}
	
	class AlphaDTO	{
	  double pearson_alpha; 
	  double spearman_alpha;
	}
	
	private ProcessBuilder pb;
	private Process p;
	private boolean isWindows;
	private static IOHandler IOH;
	private static Checkups Checker;
	private static Operations Ops;
}
