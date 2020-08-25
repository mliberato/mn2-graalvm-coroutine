# mn2-graalvm-coroutine

Steps to reproduce bug in Micronaut 2.0.1 + GraalVM 20.1.0 + Kotlin 1.3.72 + coroutineScope

It is not possible to use coroutineScope to make async calls to other suspend functions in parallel.

# Clone & build
```shell
git clone https://github.com/mliberato/mn2-graalvm-coroutine.git
cd mn2-graalvm-coroutine
./gradlew assemble && ./docker-build.sh
```

# Run standard kotlin (port 8080):
```shell
./gradlew run
```

# Run native-image in docker (port 8081):
```shell
docker run -p 8081:8080 demo
```

# Test standard & native-image
```shell
curl http://localhost:8080/serial
curl http://localhost:8080/parallel

curl http://localhost:8081/serial
curl http://localhost:8081/parallel
```

The execution in parallel under native-image does not work. Full stack trace:
```
Unexpected error occurred: Could not initialize class kotlin.internal.PlatformImplementationsKt
java.lang.NoClassDefFoundError: Could not initialize class kotlin.internal.PlatformImplementationsKt
	at com.oracle.svm.core.hub.ClassInitializationInfo.initialize(ClassInitializationInfo.java:214)
	at java.lang.Class.ensureInitialized(DynamicHub.java:499)
	at kotlin.ExceptionsKt__ExceptionsKt.addSuppressed(Exceptions.kt:48)
	at kotlinx.coroutines.JobSupport.addSuppressedExceptions(JobSupport.kt:1503)
	at kotlinx.coroutines.JobSupport.finalizeFinishingState(JobSupport.kt:215)
	at kotlinx.coroutines.JobSupport.tryMakeCompletingSlowPath(JobSupport.kt:903)
	at kotlinx.coroutines.JobSupport.tryMakeCompleting(JobSupport.kt:860)
	at kotlinx.coroutines.JobSupport.makeCompletingOnce$kotlinx_coroutines_core(JobSupport.kt:825)
	at kotlinx.coroutines.intrinsics.UndispatchedKt.startUndispatchedOrReturn(Undispatched.kt:209)
	at kotlinx.coroutines.CoroutineScopeKt.coroutineScope(CoroutineScope.kt:194)
	at com.example.Controller.parallel(Application.kt:30)
	at com.example.$ControllerDefinition$$exec2.invokeInternal(Unknown Source)
	at io.micronaut.context.AbstractExecutableMethod.invoke(AbstractExecutableMethod.java:146)
	at io.micronaut.context.DefaultBeanContext$4.invoke(DefaultBeanContext.java:469)
	at io.micronaut.web.router.AbstractRouteMatch.execute(AbstractRouteMatch.java:312)
	at io.micronaut.web.router.RouteMatch.execute(RouteMatch.java:118)
	at io.micronaut.http.server.netty.RoutingInBoundHandler.lambda$buildResultEmitter$9(RoutingInBoundHandler.java:1352)
	at io.reactivex.internal.operators.flowable.FlowableDefer.subscribeActual(FlowableDefer.java:35)
	at io.reactivex.Flowable.subscribe(Flowable.java:14918)
	at io.reactivex.Flowable.subscribe(Flowable.java:14865)
	at io.micronaut.http.server.context.ServerRequestContextFilter.lambda$doFilter$0(ServerRequestContextFilter.java:62)
	at io.reactivex.internal.operators.flowable.FlowableFromPublisher.subscribeActual(FlowableFromPublisher.java:29)
	at io.reactivex.Flowable.subscribe(Flowable.java:14918)
	at io.reactivex.Flowable.subscribe(Flowable.java:14868)
	at io.micronaut.http.server.netty.RoutingInBoundHandler.lambda$buildExecutableRoute$5(RoutingInBoundHandler.java:1056)
	at io.micronaut.web.router.DefaultUriRouteMatch$1.execute(DefaultUriRouteMatch.java:80)
	at io.micronaut.web.router.RouteMatch.execute(RouteMatch.java:118)
	at io.micronaut.http.server.netty.RoutingInBoundHandler.handleRouteMatch(RoutingInBoundHandler.java:686)
	at io.micronaut.http.server.netty.RoutingInBoundHandler.channelRead0(RoutingInBoundHandler.java:548)
	at io.micronaut.http.server.netty.RoutingInBoundHandler.channelRead0(RoutingInBoundHandler.java:143)
	at io.netty.channel.SimpleChannelInboundHandler.channelRead(SimpleChannelInboundHandler.java:99)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.netty.channel.SimpleChannelInboundHandler.channelRead(SimpleChannelInboundHandler.java:102)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.netty.handler.codec.MessageToMessageDecoder.channelRead(MessageToMessageDecoder.java:102)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.netty.channel.ChannelInboundHandlerAdapter.channelRead(ChannelInboundHandlerAdapter.java:93)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.micronaut.http.netty.stream.HttpStreamsHandler.channelRead(HttpStreamsHandler.java:197)
	at io.micronaut.http.netty.stream.HttpStreamsServerHandler.channelRead(HttpStreamsServerHandler.java:121)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.netty.handler.codec.MessageToMessageDecoder.channelRead(MessageToMessageDecoder.java:102)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.netty.handler.codec.MessageToMessageDecoder.channelRead(MessageToMessageDecoder.java:102)
	at io.netty.handler.codec.MessageToMessageCodec.channelRead(MessageToMessageCodec.java:111)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.netty.channel.ChannelInboundHandlerAdapter.channelRead(ChannelInboundHandlerAdapter.java:93)
	at io.netty.handler.codec.http.HttpServerKeepAliveHandler.channelRead(HttpServerKeepAliveHandler.java:64)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.netty.handler.flow.FlowControlHandler.dequeue(FlowControlHandler.java:191)
	at io.netty.handler.flow.FlowControlHandler.channelRead(FlowControlHandler.java:153)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.netty.channel.CombinedChannelDuplexHandler$DelegatingChannelHandlerContext.fireChannelRead(CombinedChannelDuplexHandler.java:436)
	at io.netty.handler.codec.ByteToMessageDecoder.fireChannelRead(ByteToMessageDecoder.java:321)
	at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:295)
	at io.netty.channel.CombinedChannelDuplexHandler.channelRead(CombinedChannelDuplexHandler.java:251)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.netty.handler.timeout.IdleStateHandler.channelRead(IdleStateHandler.java:286)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357)
	at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379)
	at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365)
	at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919)
	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:163)
	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:714)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:650)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:576)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493)
	at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989)
	at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74)
	at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30)
	at java.lang.Thread.run(Thread.java:834)
	at com.oracle.svm.core.thread.JavaThreads.threadStartRoutine(JavaThreads.java:517)
	at com.oracle.svm.core.posix.thread.PosixJavaThreads.pthreadStartRoutine(PosixJavaThreads.java:193)
```
