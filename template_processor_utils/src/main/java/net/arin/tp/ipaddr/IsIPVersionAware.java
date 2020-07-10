package net.arin.tp.ipaddr;

public interface IsIPVersionAware
{
    boolean isV4();

    boolean isV6();

    IPVersion getVersion();
}
