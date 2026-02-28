package com.example.helloworld;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldMod implements ModInitializer {
    public static final String MOD_ID = "helloworld";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // 标记是否已经发送过消息
    private static boolean hasSentMessage = false;

    @Override
    public void onInitialize() {
        LOGGER.info("Hello World mod 初始化成功！");
        
        // 在服务器启动时输出
        System.out.println("Hello World! 来自服务器的问候");
        
        // 由于1.21版本的API变化，我们使用客户端加载事件
        // 注意：这个模组需要同时安装fabric-api才能使用这些事件
        setupClientEvents();
    }
    
    private void setupClientEvents() {
        // 使用客户端tick事件来检测玩家是否已加入世界
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // 确保玩家存在且世界已加载，且还未发送过消息
            if (client.player != null && client.world != null && !hasSentMessage) {
                // 在聊天栏发送彩色消息
                Text message = Text.literal("")
                    .append(Text.literal("Hello World! ").formatted(Formatting.GREEN, Formatting.BOLD))
                    .append(Text.literal("欢迎来到我的世界！").formatted(Formatting.YELLOW));
                
                client.player.sendMessage(message, false);
                
                // 同时也发送到系统消息栏（热栏上方）
                client.player.sendMessage(
                    Text.literal("模组加载成功！").formatted(Formatting.AQUA), 
                    true
                );
                
                // 标记已发送，避免重复发送
                hasSentMessage = true;
                
                // 记录日志
                LOGGER.info("已向玩家发送欢迎消息");
            }
        });
        
        // 监听玩家发送的消息（可选功能）
        ClientSendMessageEvents.ALLOW_CHAT.register(message -> {
            if (message.equalsIgnoreCase("hello")) {
                if (net.minecraft.client.MinecraftClient.getInstance().player != null) {
                    net.minecraft.client.MinecraftClient.getInstance().player.sendMessage(
                        Text.literal("你也好啊！").formatted(Formatting.LIGHT_PURPLE),
                        false
                    );
                }
            }
            return true; // 允许消息正常发送
        });
    }
}