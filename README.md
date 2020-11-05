# video
<h3>这是一个播放摄像头的工具，可进行自由扩展，主要用来在h5上面播放rtsp流</h3>
<h3>在IE11以及以下由于不支持audio的api，所以还是采用的事falsh</h3>
<h3>可以扩展agent的数量提高播放数量，可以自动负载均衡</h3>
<h4>在IE上面需要配置nginx-rtmp,因为采用了rtmp的格式</h4>
<h4>需要安装ffmpeg（windows、linux），并配置环境变量到path下面</h4>
<h4>需要安装nodejs，并配置环境变量到path下面</h4>
<h4>配置相关参数</h4>
<h4>然后启动video-regist ->video-agent -> video-center</h4>
