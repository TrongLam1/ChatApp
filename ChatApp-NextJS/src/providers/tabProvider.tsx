'use client'
import React, { useContext, useState } from "react";
import { createContext } from "react";

const defaultTab = 'friends';

const TabContext = createContext<any>(defaultTab);

export const TabProvider = ({ children }: { children: React.ReactNode }) => {
    const [tab, setTab] = useState<string>(defaultTab);
    return (
        <TabContext.Provider value={{ tab, setTab }}>
            {children}
        </TabContext.Provider>
    );
};

export const useTab = () => useContext(TabContext);