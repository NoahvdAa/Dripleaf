package me.noahvdaa.dripleaf.util;

import me.noahvdaa.dripleaf.AppConstants;
import me.noahvdaa.dripleaf.DripleafServer;
import me.noahvdaa.dripleaf.net.ConnectionStatus;
import org.bstats.MetricsBase;
import org.bstats.charts.CustomChart;
import org.bstats.charts.DrilldownPie;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bstats.json.JsonObjectBuilder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Metrics {

	private final MetricsBase metricsBase;

	private boolean enabled;
	private String serverUUID;
	private boolean logErrors = false;
	private boolean logSentData;
	private boolean logResponseStatusText;

	/**
	 * Creates a new Metrics instance.
	 *
	 * @param serviceId The id of the service.
	 *                  It can be found at <a href="https://bstats.org/what-is-my-plugin-id">What is my plugin id?</a>
	 */
	public Metrics(int serviceId) {
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

	private void appendServiceData(JsonObjectBuilder ignoredBuilder) {
		// This is intentionally kept empty.
	}

	public static class DripleafMetrics {
		public static void start(DripleafServer server) {
			Metrics metrics = new Metrics(11722);

			metrics.addCustomChart(new SingleLineChart("players", () -> (int) server.connections.stream().filter(c -> c.status == ConnectionStatus.PLAYING).count()));
			metrics.addCustomChart(new SimplePie("bungeecord", () -> server.isBungeecordModeMode() ? "true" : "false"));
			metrics.addCustomChart(new SimplePie("dripleafVersion", () -> AppConstants.version));
			metrics.addCustomChart(new DrilldownPie("java_version", () -> {
				// Shamelessly "borrowed" from Paper.
				// https://github.com/PaperMC/Paper/blob/a79616b9cc5aeb2860fe5eaa1c7f33344af52303/patches/server/0007-Paper-Metrics.patch#L624
				Map<String, Map<String, Integer>> map = new HashMap<>();
				String javaVersion = System.getProperty("java.version");
				Map<String, Integer> entry = new HashMap<>();
				entry.put(javaVersion, 1);

				// http://openjdk.java.net/jeps/223
				// Java decided to change their versioning scheme and in doing so modified the java.version system
				// property to return $major[.$minor][.$secuity][-ea], as opposed to 1.$major.0_$identifier
				// we can handle pre-9 by checking if the "major" is equal to "1", otherwise, 9+
				String majorVersion = javaVersion.split("\\.")[0];
				String release;

				int indexOf = javaVersion.lastIndexOf('.');

				if (majorVersion.equals("1")) {
					release = "Java " + javaVersion.substring(0, indexOf);
				} else {
					// of course, it really wouldn't be all that simple if they didn't add a quirk, now would it
					// valid strings for the major may potentially include values such as -ea to deannotate a pre release
					Matcher versionMatcher = Pattern.compile("\\d+").matcher(majorVersion);
					if (versionMatcher.find()) {
						majorVersion = versionMatcher.group(0);
					}
					release = "Java " + majorVersion;
				}
				map.put(release, entry);

				return map;
			}));
		}
	}

}