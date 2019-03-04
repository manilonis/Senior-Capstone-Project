/**
 * Michael E Anilonis
 * Mar 4, 2019
 */
package api_backend;

import static spark.Spark.*;

public class MyAPI {
	public static void main(String[] args) {
		get("/hello", (req, res)->"Hello, world");
	}
}
