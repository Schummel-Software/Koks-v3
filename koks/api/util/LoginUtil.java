package koks.api.util;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.api.response.Account;
import com.thealtening.api.retriever.AsynchronousDataRetriever;
import com.thealtening.api.retriever.BasicDataRetriever;
import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import koks.Koks;
import koks.api.Methods;
import net.minecraft.util.Session;

import java.net.Proxy;

/**
 * @author kroko
 * @created on 07.10.2020 : 17:14
 */
public class LoginUtil extends Methods {

    public String status = "waiting...";

    public void login(String token) {
        if(token.contains("@alt")) {
            try {
                TheAlteningAuthentication theAlteningAuthentication = TheAlteningAuthentication.theAltening();
                theAlteningAuthentication.updateService(AlteningServiceType.THEALTENING);
                YggdrasilUserAuthentication service = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
                service.setUsername(token);
                service.setPassword(Koks.getKoks().NAME);

                service.logIn();
                status = "Logged into §e" + service.getSelectedProfile().getName();
                mc.session = new Session(service.getSelectedProfile().getName(), service.getSelectedProfile().getId().toString(), service.getAuthenticatedToken(), "LEGACY");
            } catch (Exception e) {
                status = "§c§lError: §cAccount doesn't working";
            }
        }
    }

    public void login(String email, String password) {
        try {
            TheAlteningAuthentication theAlteningAuthentication = TheAlteningAuthentication.mojang();
            YggdrasilUserAuthentication service = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
            service.setUsername(email);
            service.setPassword(password);
            service.logIn();
            theAlteningAuthentication.updateService(AlteningServiceType.MOJANG);
            status = "Logged into §e" + service.getSelectedProfile().getName();
            mc.session = new Session(service.getSelectedProfile().getName(), service.getSelectedProfile().getId().toString(), service.getAuthenticatedToken(), "LEGACY");
        } catch (Exception e) {
            status = "§c§lError: §cAccount doesn't working";
        }
    }

    public void generate(String apiToken) {
        try {
            BasicDataRetriever basicDataRetriever = new BasicDataRetriever(apiToken);
            TheAlteningAuthentication theAlteningAuthentication = TheAlteningAuthentication.theAltening();
            basicDataRetriever.updateKey(apiToken);
            theAlteningAuthentication.updateService(AlteningServiceType.THEALTENING);
            AsynchronousDataRetriever asynchronousDataRetriever = basicDataRetriever.toAsync();
            Account account = asynchronousDataRetriever.getAccount();
            YggdrasilUserAuthentication service = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);


            service.setUsername(account.getToken());
            service.setPassword(Koks.getKoks().NAME);

            service.logIn();
            status = "Logged into §e" + service.getSelectedProfile().getName();
            mc.session = new Session(service.getSelectedProfile().getName(), service.getSelectedProfile().getId().toString(), service.getAuthenticatedToken(), "LEGACY");
        } catch (Exception e) {
            status = "§c§lError: §cAccount doesn't working";
        }
    }
}