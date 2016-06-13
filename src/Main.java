import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");

                    HashMap m = new HashMap();
                    if (username == null) {
                        return new ModelAndView(m, "login.html");
                    } else {
                        User user = users.get(username);
                        m.put("mobs", user.mobs);
                        return new ModelAndView(m, "home.html");
                    }
                },
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/login",
                (request, response) -> {
                    String name = request.queryParams("username");
                    String pass = request.queryParams("password");
                    if (name == null || pass == null) {
                        throw new Exception("name or password blank");
                    }

                    User user = users.get(name);
                    if (user == null) {
                        user = new User(name, pass);
                        users.put(name, user);
                    } else if (!pass.equals(user.password)) {
                        throw new Exception("wrong password");
                    }

                    Session session = request.session();
                    session.attribute("username", name);

                    response.redirect("/");
                    return "";
                }

        );
        Spark.post(
                "/create-mob",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null) {
                        throw new Exception("Not logged in");
                    }
                    String mb = request.queryParams("mob");
                    String location = request.queryParams("location");
                    String threat = request.queryParams("threat");
                    if (mb == null) {
                        throw new Exception("invalid form fields");
                    }

                    User user = users.get(username);
                    if (user == null) {
                        throw new Exception("User does not exist");
                    }

                    Mob r = new Mob(mb, location, threat);
                    user.mobs.add(r);

                    response.redirect("/");
                    return "";

                }

        );
        Spark.post(
                "/logout",
                (request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                }
        );
        Spark.post(
                "/delete-mob",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null) {
                        throw new Exception("Not logged in");
                    }
                    int id = Integer.valueOf(request.queryParams("id"));

                    User User = users.get(username);
                    if (id <= 0 || id - 1 >= User.mobs.size() - 1) ;
                    {

                        User.mobs.remove(id - 1);
                    }
                    response.redirect("/");
                    return "";
                }

        );
        Spark.post(
                "/edit-mob",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null) {
                        throw new Exception("Not logged in");

                    }
                    int id = Integer.valueOf(request.queryParams("mobEdit"));
                    String text = request.queryParams("mobField");

                    User User = users.get(username);
                    if (id <= 0 || id - 1 >= User.mobs.size() - 1);
                    {
                        Mob m = User.mobs.get(id - 1);
                        m.mob = text;
                    }
                    response.redirect("/");
                    return "";
                }
        );
    }
}





