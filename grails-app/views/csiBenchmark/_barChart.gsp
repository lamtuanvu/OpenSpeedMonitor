<div id="chart-container">
    <div class="in-chart-buttons hidden">
        <a href="#adjustBarchartModal" id="adjust-barchart-modal" data-toggle="modal" data-target="#adjustBarchartModal"
           onclick="initModalDialogValues()">
            <i class="fa fa-sliders"></i>
        </a>
        <a href="#downloadAsPngModal" id="download-as-png-button"
           data-toggle="modal" role="button" onclick="setDefaultValues('svg-container')"
           title="${message(code: 'de.iteratec.ism.ui.button.save.name', default:'Download as PNG')}">
            <i class="fa fa-download"></i>
        </a>
    </div>

    <div id="svg-container">
        <svg></svg>
    </div>
</div>
<g:render template="adjustBarchartModal"/>
<asset:script type="text/javascript">
    $(window).load(function() {
      OpenSpeedMonitor.postLoader.loadJavascript('<g:assetPath src="/csiBenchmark/csiBenchmarkChart.js" />',true,'barchart')
    });
</asset:script>
