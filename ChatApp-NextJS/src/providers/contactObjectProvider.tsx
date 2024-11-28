'use client'
import React, { useContext, useState } from "react";
import { createContext } from "react";

const defaultContactObject: IContactObject = {
    id: null,
    name: null,
    avatar: null,
    isGroup: null,
    members: null,
};

const ContactObjectContext = createContext<any>(defaultContactObject);

export const ContactObjectProvider = ({ children }: { children: React.ReactNode }) => {
    const [contactObject, setContactObject] = useState<any>();
    return (
        <ContactObjectContext.Provider value={{ contactObject, setContactObject }}>
            {children}
        </ContactObjectContext.Provider>
    );
};

export const useContactObject = () => useContext(ContactObjectContext);