package com.stelios.cakenaysh.Managers;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class PacketManager {

    public void inject(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler(){
            @Override
            public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {

                ////remove the damage_indicator particles
                //if the packet is a particle packet
                if (packet instanceof ClientboundLevelParticlesPacket) {

                    //get the packet
                    ClientboundLevelParticlesPacket particlesPacket = (ClientboundLevelParticlesPacket) packet;

                    //block the packet if the particle is damage_indicator
                    if (particlesPacket.getParticle() == ParticleTypes.DAMAGE_INDICATOR) {
                        return;
                    }

                }

                //send the packet
                super.write(ctx, packet, promise);
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
                super.channelRead(ctx, packet);
            }
        };

        //get the connection
        Connection connection = getConnection((CraftPlayer) player);

        ChannelPipeline pipeline = connection.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
    }


    public void unInject(Player player){

        //get the connection
        Connection connection = getConnection((CraftPlayer) player);

        connection.channel.eventLoop().submit(() -> {
            connection.channel.pipeline().remove(player.getName());
            return null;
        });
    }


    private static Connection getConnection(CraftPlayer player) {
        ServerGamePacketListenerImpl serverGamePacketListener = player.getHandle().connection;
        Connection connection;
        try {
            Field field = serverGamePacketListener.getClass().getDeclaredField("h");
            field.setAccessible(true);
            connection = (Connection) field.get(serverGamePacketListener);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

}
