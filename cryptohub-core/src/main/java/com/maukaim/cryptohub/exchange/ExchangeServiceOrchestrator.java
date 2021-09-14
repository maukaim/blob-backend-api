package com.maukaim.cryptohub.exchange;

import com.maukaim.cryptohub.plugins.api.exchanges.ExchangeService;
import com.maukaim.cryptohub.plugins.api.exchanges.exception.ExchangeConnectionException;
import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameter;
import com.maukaim.cryptohub.plugins.api.exchanges.model.ConnectionParameters;
import com.maukaim.cryptohub.plugins.core.model.module.ModuleInfo;
import com.maukaim.cryptohub.plugins.core.model.module.ModuleProvider;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExchangeServiceOrchestrator {
    //TODO: Provide la list de tous les ExchangeServices et permet d'en initier?

    List<ModuleInfo> getAvailableExchangesInfo();
    Optional<ModuleProvider<? extends ExchangeService>> getExchangeProvider(String pluginId, String exchangeName);
    Map<String, List<ConnectionParameter>> getConnectionParameters(ModuleProvider<? extends ExchangeService> exchangeProvider);
    ExchangeWrapper connect(ModuleProvider<? extends ExchangeService> mp,
                            ConnectionParameters connectionParameters)
            throws ExchangeConnectionException;

    Optional<ExchangeWrapper> getExchange(String wrapperId);
}


/*
0 - User veut initier une connection, Front demande liste des Exchange disponibles
1 - tu get les ModuleProvider<ExchangeService> pour avoir la lsite des dispo actuellement
2 - tu en extrait les Informations d'exchange pour que le User fasse son choix
3 - Back repond au front
 */

/*
0 - User choisit un Exchange, Front demande au Back comment initier cette connexion
1 - Back request le ModuleProvider de cet Exchange, et get son PreProcess
2 - PreProcess indique quels sont les parametres de connexion
3 - Back repond au front
 */

/*
0 - User rempli 1 choix de connection, Front demande au Back d'initier la connection avec ca !
1 - Back tente de se connecter avec ce param de co,
2 - Back creer un Manager avec un Id et le store
2 - Back repond au front
 */
