<html>
	<head>
		<title><g:message code="error.404.title"/></title>
		<meta name="layout" content="layoutOsm" />
		<g:set var="layout_nomainmenu"		value="${true}" scope="request"/>
		<g:set var="layout_nosecondarymenu"	value="${true}" scope="request"/>
	</head>

<body>
	<content tag="header">
		<!-- Empty Header -->
	</content>
	
  	<section id="Error">
		<div class="big-message">
			<div class="container">
				<h1>
					<g:message code="error.404.callout"/>
				</h1>
				<h2>
					<g:message code="error.404.title"/>
				</h2>
				<p>
					<g:message code="error.404.message"/>
				</p>
				
				<div class="actions">
					<a href="${createLink(uri: '/')}" class="btn btn-lg btn-primary">
						<i class="fas fa-chevron-left"></i>
						<g:message code="error.button.backToHome"/>
					</a>
					<a href="${createLink(uri: '/contact')}" class="btn btn-lg btn-success">
						<i class="fas fa-envelope"></i>
						<g:message code="error.button.contactSupport"/>
					</a>					
				</div>
			</div>
		</div>
	</section>
  
  
  </body>
</html>