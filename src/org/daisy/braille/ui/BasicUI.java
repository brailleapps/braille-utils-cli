/*
 * Braille Utils (C) 2010-2011 Daisy Consortium 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.daisy.braille.ui;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.daisy.braille.pef.FileTools;
import org.daisy.cli.AbstractUI;
import org.daisy.cli.Argument;
import org.daisy.cli.CommandParserResult;
import org.daisy.cli.Definition;
import org.daisy.cli.ExitCode;
import org.daisy.cli.OptionalArgument;
import org.daisy.cli.SwitchArgument;

/**
 * Provides a basic command line UI for core functionality in
 * org.daisy.braille.
 * @author Joel Håkansson
 */
public class BasicUI extends AbstractUI {
	public static final String emboss = "emboss";
	public static final String text2pef = "text2pef";
	public static final String pef2text = "pef2text";
	public static final String validate = "validate";
	public static final String split = "split";
	public static final String merge = "merge";
	public static final String generate = "generate";
	public static final String list = "list";
	public static final String find = "find";
	//public static final String clear = "clear";
	//public static final String setup = "setup";
	public static final String help = "help";
	public static final String inspect = "inspect";
	
	protected static final String META_KEY = "meta";
	private static final String VERSION_KEY = "version";

	private final String[] args;
	private final Logger logger;
	
	private final Map<String, Class<? extends AbstractUI>> commands;
	private final List<Definition> values;
	
	private static final ManifestRetriever retriever = new ManifestRetriever(BasicUI.class);
	private static final String BUILD_IDENTIFIER = retriever.getManifest().getMainAttributes().getValue("Repository-Revision");
	private static final String SYSTEM_VERSION = retriever.getManifest().getMainAttributes().getValue("Implementation-Version");
	
	/**
	 * Creates a new Basic UI
	 * @param args the application arguments
	 */
	public BasicUI(String[] args) {
		logger = Logger.getLogger(BasicUI.class.getCanonicalName());
		logger.fine(System.getProperties().toString());
		this.args = args;
		this.values = new ArrayList<Definition>();
		this.commands = new HashMap<>();
		
		values.add(new Definition(help, "Without additional arguments, this text is displayed. To get help on a specific command, type help <command>"));
		putCommand(emboss, "emboss a PEF-file", EmbossPEF.class);
		putCommand(text2pef, "convert text to pef", TextParser.class);
		putCommand(pef2text, "convert pef to text", PEFParser.class);
		putCommand(validate, "validate a PEF-file", ValidatePEF.class);
		putCommand(split, "split a PEF-file into several single volume files", SplitPEF.class);
		putCommand(merge, "merge several single volume PEF-files into one", MergePEF.class);
		putCommand(generate, "generate a random PEF-file for testing", GeneratePEF.class);
		putCommand(list, "lists stuff", ListStuff.class);
		//
		putCommand(inspect, "lists metadata about a PEF-book", PEFInfo.class);
		putCommand(find, "finds PEF-books", FindPEF.class);
		parser.addSwitch(new SwitchArgument('v', VERSION_KEY, META_KEY, VERSION_KEY, "Displays version information."));
		/*
 			values.add(new Definition(clear, "clear settings"));
			values.add(new Definition(setup, "setup"));
			values.add(new Definition(help, "help"));
			case CLEAR: { EmbossPEF ui = new EmbossPEF(); ui.clearSettings(); break; }
			case SETUP: { EmbossPEF ui = new EmbossPEF(); ui.setup(); break; }
		}*/
	}
	
	protected void putCommand(String cmd, String desc, Class<? extends AbstractUI> c) {
		values.add(new Definition(cmd, desc));
		commands.put(cmd, c);
	}
	
	/**
	 * Sets the context class loader to an URLClassLoader containing the jars found in
	 * the specified path. 
	 * @param dir the directory to search for jar-files.
	 */
	public void setPluginsDir(File dir) {
		// list jars and convert to URL's
		URL[] jars = FileTools.toURL(FileTools.listFiles(dir, ".jar"));
		for (URL u : jars) {
			logger.info("Found jar " + u);
		}
		// set context class loader
		if (jars.length>0) {
			Thread.currentThread().setContextClassLoader(new URLClassLoader(jars));
		}
	}

	/**
	 * Runs the application.
	 * @throws Exception if something bad happens
	 */
	public void run() throws Exception {
		if (args.length<1) {
			System.out.println("Expected at least one argument.");
			System.out.println();
			displayHelp(System.out);
			System.exit(-ExitCode.MISSING_ARGUMENT.ordinal());
		}
		setPluginsDir(new File("plugins"));
		if (help.equalsIgnoreCase(args[0])) {
			if (args.length>=2) {
				Class<? extends AbstractUI> clazz = commands.get(args[1]);
				if (clazz!=null) {
					AbstractUI ui = clazz.newInstance();
					ui.displayHelp(System.out);
					exitWithCode(ExitCode.OK);
				} else {
					System.out.println("Unknown argument '" + args[1] + "'");
					displayHelp(System.out);
					System.exit(-ExitCode.UNKNOWN_ARGUMENT.ordinal());
				}
			}
			displayHelp(System.out);
		} else {
			CommandParserResult result = parser.parse(args);
			if (result.getRequired().isEmpty() && VERSION_KEY.equals(result.getOptional().get(META_KEY))) {
				System.out.println("--- " + getName() + " ---");
				System.out.println("Version: " + (getVersion()!=null?getVersion():"N/A"));
				System.out.println("Build: " + (getBuildIdentifier()!=null?getBuildIdentifier():"N/A"));
				BasicUI.exitWithCode(ExitCode.OK);
			}
			Class<? extends Object> clazz = commands.get(args[0]);
			if (clazz!=null) {
				Method method = clazz.getMethod("main", new Class[]{String[].class});
				method.invoke(null, (Object)getArgsSubList(1));
			} else {
				System.out.println("Unknown argument '" + args[0] + "'");
				displayHelp(System.out);
				System.exit(-ExitCode.UNKNOWN_ARGUMENT.ordinal());
			}
		}
	}
	
	/**
	 * Command line entry point
	 * @param args the application arguments
	 * @throws Exception if something goes wrong
	 */
	public static void main(String[] args) throws Exception {
		System.err.println("Note: This project has been merged into Dotify (https://github.com/brailleapps/dotify-cli).\n"
				+ "This application will not be updated anymore.\nPlease use Dotify from now on.\n");
		BasicUI ui = new BasicUI(args);
		ui.run();
	}

	private String[] getArgsSubList(int offset) {
		int len = args.length-offset;
		if (len==0) {
			// no args left
			return new String[]{};
		} else if (len<0) {
			// too few args
			throw new IllegalArgumentException("New array has a negative size");
		}
		String[] args2 = new String[len];
		System.arraycopy(args, offset, args2, 0, len);
		return args2;
	}

	@Override
	public String getName() {
		return "braille-utils";
	}
	
	@Override
	public String getDescription() {
		return "Provides tools for managing PEF-files.";
	}

	@Override
	public List<Argument> getRequiredArguments() {
		ArrayList<Argument> ret = new ArrayList<Argument>();
		ret.add(new Argument("command", "the command to run", values));
		return ret;
	}

	@Override
	public List<OptionalArgument> getOptionalArguments() {
		return null;
	}

	public String getVersion() {
		return SYSTEM_VERSION;
	}
	
	public String getBuildIdentifier() {
		return BUILD_IDENTIFIER;
	}
}
