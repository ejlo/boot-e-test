with import <nixpkgs> {}; {
  gruntEnv = stdenv.mkDerivation rec {
    name = "grunt-env";
    src = ./.;
    buildInputs = with nodePackages; [
      nodejs
      grunt-cli
      bower
      # use local electron
      #electron
      boot
      leiningen
    ];
    shellHook = ''
      boot -u
    '';
  };
}
