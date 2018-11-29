<%@ page import="java.io.*,java.util.*,javax.servlet.ServletContext,com.awesome.ShoeMail" %>
<%@ include file="header.jsp" %>
<%@ include file="products.jsp" %>

<% 	String pageget = request.getParameter("page");
	if (pageget == null) { %>
		<p>Welcome to e-Coms, an online store where you can wholesale order shoes for great great prices!</p>
		<p>Products we sell: </p>
		<div id="productlist">
			<% 
				for(String product : products){
					String[] productData = product.split("~"); 
			%>
					<div class="product">
						<h3><%= productData[2] %></h3>
						<img alt="<%= productData[2] %>" src="<%= productData[3] %>" />
						<div class="price">
							Price: $<%= productData[1] %> &nbsp;&nbsp;&nbsp;
							<button class="addtocart" value="<%= productData[0] %>">Add to Cart</button>
						</div>
					</div>
			<%	} %>
		</div>
		<div id="dialog-form" title="Place an order">
			<p class="validateTips">All form fields are required.</p>
			<form id="orderform" action="?page=orderprocess" method="post">
				<fieldset>
					<label for="forename">Forename</label>
					<input type="text" name="forename" id="forename" class="text ui-widget-content ui-corner-all" />
					<label for="surname">Surname</label>
					<input type="text" name="surname" id="surname" class="text ui-widget-content ui-corner-all" />
					<label for="email">Email</label>
					<input type="text" name="email" id="email" value="" class="text ui-widget-content ui-corner-all" />
					<label for="address">Shipping Address</label>
					<textarea rows="5" cols="25" name="address" id="address" class="text ui-widget-content ui-corner-all"></textarea>
					<label for="item">Item: (CTRL+Click for multiple items)</label>
					<select multiple="multiple" name="item" id="item" class="text ui-widget-content ui-corner-all">
						<%
						for(String product : products){
							String[] productData = product.split("~");
							out.println("<option value='"+productData[0]+"'>"+productData[2]+":  $"+productData[1]+"</option>");
						}
						%>
					</select>
				</fieldset>
			</form>
		</div>
		<div id="cartpopup"></div>
<%	} else if ( pageget.equals("about") ) { %>
			<p class="motto">In close cooperation with some of the best and greatest desginers and models of the world we strive to provide the best, the most luxuries, the most exclusive of foot wear.</p>
			<p>e-Coms was first established in 2004 as a wholesale market for luxury goods of all types. In 2010, e-Coms joined together with the LVMH and PPR, completely changing its business structure and excusivity of its goods. This year, in cooperation with Stuart Weitzman, we provide here the most exlusive of his shoes for purchase.</p>
<%	} else if ( pageget.equals("orderprocess") ) { 
		int totalCost = 0;
		
		PrintStream pstr = new PrintStream(new BufferedOutputStream(new FileOutputStream(getServletContext().getRealPath("/")+"purchases.txt",true)));
		pstr.println("Name: "+request.getParameter("forename")+" "+request.getParameter("surname"));
		pstr.println("Email: "+request.getParameter("email"));
		pstr.println("Address: "+request.getParameter("address"));
		String [] items = request.getParameterValues("item");
		for(String item : items) {
			String[] productData = products[Integer.parseInt(item)].split("~");
			pstr.println("Purchased Item "+item+": "+productData[2]);
			pstr.println("Purchase Price: $"+productData[1]);
			totalCost = totalCost + Integer.parseInt(productData[1]);
		}
		pstr.println("Total Price: $"+totalCost+"\n\n");
		pstr.close();
		
		String htmlOrderConf = "<br /><h2>Order Confirmed!</h2><h4>Thankyou for your custom.</h4><br />" +
		"<br />Name: "+request.getParameter("forename")+" "+request.getParameter("surname")+"<br />"+
		"Email: "+request.getParameter("email")+"<br />"+
		"Address: "+request.getParameter("address")+"<br /><br />";
		for(String item : items) {
			String[] productData = products[Integer.parseInt(item)].split("~");
			htmlOrderConf += "Purchased Item "+item+": "+productData[2]+"<br />";
			htmlOrderConf += "Purchase Price: $"+productData[1]+"<br /><br />";
		}
		htmlOrderConf += "Total Price: $"+totalCost;
		
		ShoeMail shoeMailObject = new ShoeMail();
		String shoeMailOutput = shoeMailObject.sendMail(new String[]{
				"mail.andrewbeveridge.co.uk",
				"25",
				"shoemail@andrewbeveridge.co.uk",
				"shoemail",
				"shoemail@andrewbeveridge.co.uk",
				request.getParameter("email"),
				
				"To: "+request.getParameter("forename")+" "+request.getParameter("surname")+"<"+request.getParameter("email")+">\n" +
				"From: e-Coms Shoes <shoemail@andrewbeveridge.co.uk>\n" +
				"Subject: Your e-Coms order confirmation\n" +
				"MIME-Version: 1.0\n" +
				"Content-Type: text/html; charset=ISO-8859-1\n" +
				"<html><body>" +
				"<h1>e-Coms Shoes</h1>" +
				"<b>Dear "+request.getParameter("forename")+",</b><br /><br />" +
				htmlOrderConf+
				"<br /><br />Kind Regards,<br />" +
				"e-Coms Shoes<br />" +
				"(Mailed by ShoeMail)<br />" +
				"</body></html>"
		},false);
		
		out.println(htmlOrderConf+"<br /><br />Confirmation email: "+shoeMailOutput+"<br /><br /><br /><input id='printbutton' type=\"button\" value=\"Print This Page\" onClick=\"window.print()\" /><br /><br />");
		
	} else { %>
			<p>Invalid page!</p>
<%	} %>

<%@ include file="footer.jsp" %>
