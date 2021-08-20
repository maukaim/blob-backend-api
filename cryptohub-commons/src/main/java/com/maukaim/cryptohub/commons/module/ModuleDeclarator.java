package com.maukaim.cryptohub.commons.module;

// Not implementable outside package.
//
public interface ModuleDeclarator {
    String getName();

    String getDescription();

    void activate();

    void disactivate();
}

//1 User regarde la liste des exchanges disponible
//2 Il choisi un exchange (celui d'un plugin)
//3 L'exchange dit "jai besoin de xy z parameters pour me connecter
//4 Le User fourni les parametres
//5 L'appli utilise les parametres pour creer une session
// avec l'exchange grace au plugin
