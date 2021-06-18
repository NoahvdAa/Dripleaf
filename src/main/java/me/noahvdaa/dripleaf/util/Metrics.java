package me.noahvdaa.dripleaf.util;

import me.noahvdaa.dripleaf.AppConstants;
import me.noahvdaa.dripleaf.DripleafServer;
import me.noahvdaa.dripleaf.net.ConnectionStatus;
import org.bstats.MetricsBase;
import org.bstats.charts.CustomChart;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bstats.json.JsonObjectBuilder;

import java.io.*;
import java.util.Properties;
import java.util.UUID;

public class Metrics {

	final DripleafServer server;
	final MetricsBase metricsBase;

	private boolean enabled;
	private String serverUUID;
	private boolean logErrors = false;
	private boolean logSentData;
	private boolean logResponseStatusText;

	/**
	 * Creates a new Metrics instance.
	 *
	 * @param server    Your server instance.
	 * @param serviceId The id of the service.
	 *                  It can be found at <a href="https://bstats.org/what-is-my-plugin-id">What is my plugin id?</a>
	 */
	public Metrics(DripleafServer server, int serviceId) {
		this.server = server;

		try {
			loadConfig();
		} catch (IOException e) {
			// Failed to load configuration
			System.out.println("Failed to load bStats config!");
			e.printStackTrace();
			metricsBase = null;
			return;
		}

		metricsBase = new MetricsBase(
				"server-implementation",
				serverUUID,
				serviceId,
				enabled,
				this::appendPlatformData,
				this::appendServiceData,
				null,
				() -> true,
				(message, error) -> {
					System.out.println(message);
					error.printStackTrace();
				},
				System.out::println,
				logErrors,
				logSentData,
				logResponseStatusText
		);
	}

	/**
	 * Loads the bStats configuration.
	 */
	private void loadConfig() throws IOException {
		File bStatsFolder = new File("./bStats");
		bStatsFolder.mkdirs();
		File configFile = new File(bStatsFolder, "config.properties");
		if (!configFile.exists()) {
			writeFile(configFile,
					"# bStats (https://bStats.org) collects some basic information for plugin authors, like how",
					"# many people use their plugin and their total player count. It's recommended to keep bStats",
					"# enabled, but if you're not comfortable with this, you can turn this setting off. There is no",
					"# performance penalty associated with having metrics enabled, and data sent to bStats is fully",
					"# anonymous.",
					"enabled=true",
					"serverUuid=\"" + UUID.randomUUID() + "\"",
					"logFailedRequests=false",
					"logSentData=false",
					"logResponseStatusText=false");
		}

		Properties configuration = new Properties();
		configuration.load(new FileInputStream(configFile));

		// Load configuration
		enabled = configuration.getProperty("enabled", "true").equalsIgnoreCase("true");
		serverUUID = configuration.getProperty("serverUuid");
		logErrors = configuration.getProperty("logFailedRequests", "false").equalsIgnoreCase("true");
		logSentData = configuration.getProperty("logSentData", "false").equalsIgnoreCase("true");
		logResponseStatusText = configuration.getProperty("logResponseStatusText", "false").equalsIgnoreCase("true");
	}

	private void writeFile(File file, String... lines) throws IOException {
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
			for (String line : lines) {
				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}
		}
	}

	/**
	 * Adds a custom chart.
	 *
	 * @param chart The chart to add.
	 */
	public void addCustomChart(CustomChart chart) {
		metricsBase.addCustomChart(chart);
	}

	private void appendPlatformData(JsonObjectBuilder builder) {
		builder.appendField("javaVersion", System.getProperty("java.version"));
		builder.appendField("osName", System.getProperty("os.name"));
		builder.appendField("osArch", System.getProperty("os.arch"));
		builder.appendField("osVersion", System.getProperty("os.version"));
		builder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
	}

	private void appendServiceData(JsonObjectBuilder builder) {

	}

	public static class DripleafMetrics {
		public static void start(DripleafServer server) {
			Metrics metrics = new Metrics(server, 11722);

			metrics.addCustomChart(new SingleLineChart("players", () -> (int) server.connections.stream().filter(c -> c.status == ConnectionStatus.PLAYING).count()));

			metrics.addCustomChart(new SimplePie("dripleafVersion", () -> AppConstants.version));
		}
	}

}